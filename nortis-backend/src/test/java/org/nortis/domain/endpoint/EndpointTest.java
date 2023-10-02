package org.nortis.domain.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.apache.velocity.app.VelocityEngine;
import org.nortis.TestBase;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.template.TemplateRender;
import org.nortis.infrastructure.template.VelocityTemplateRender;
import org.springframework.context.ApplicationContext;

class EndpointTest extends TestBase {

    @BeforeEach
    void setup() {
        ApplicationContext context = Mockito.mock(ApplicationContext.class);
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Mockito.when(context.getBean(eq(TemplateRender.class))).thenReturn(new VelocityTemplateRender(ve));
        ApplicationContextAccessor.set(context);
    }

    @Test
    void testChangeEndpointName() throws DomainException {
        Endpoint endpoint = Endpoint.create(TenantId.create("TEST"), EndpointId.create("1000000001"),
                EndpointIdentifier.create("ENDPOINT"), "Test Endpoint");
        endpoint.changeEndpointName("Endpoint");

        assertThat(endpoint.getEndpointName()).isEqualTo("Endpoint");
    }

    @Test
    void testCreateTemplate() throws DomainException {
        Endpoint endpoint = Endpoint.create(TenantId.create("TEST"), EndpointId.create("1000000001"),
                EndpointIdentifier.create("ENDPOINT"), "Test Endpoint");
        endpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("subject"), BodyTemplate.create("body"));

        assertThat(endpoint.getMessageTemplateList()).hasSize(1);

        Optional<MessageTemplate> optTemplate = endpoint.getTemplate(TextType.TEXT);
        assertThat(optTemplate).isPresent();
        assertThat(optTemplate.get().getEndpointId()).isEqualTo(EndpointId.create("1000000001"));
        assertThat(optTemplate.get().getTextType()).isEqualTo(TextType.TEXT);
        assertThat(optTemplate.get().getSubjectTemplate()).isEqualTo(SubjectTemplate.create("subject"));
        assertThat(optTemplate.get().getBodyTemplate()).isEqualTo(BodyTemplate.create("body"));
    }

    @Test
    void testCreateTemplate_Duplicate() throws DomainException {
        Endpoint endpoint = Endpoint.create(TenantId.create("TEST"), EndpointId.create("1000000001"),
                EndpointIdentifier.create("ENDPOINT"), "Test Endpoint");
        endpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("subject"), BodyTemplate.create("body"));

        assertThrows(DomainException.class, () -> {
            endpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("subject"), BodyTemplate.create("body"));
        });
    }

    @Test
    void deleteTemplate() throws DomainException {
        Endpoint endpoint = Endpoint.create(TenantId.create("TEST"), EndpointId.create("1000000001"),
                EndpointIdentifier.create("ENDPOINT"), "Test Endpoint");
        endpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("subject"), BodyTemplate.create("body"));

        endpoint.deleteTemplate(TextType.TEXT);

        Optional<MessageTemplate> optMessageTemplate = endpoint.getTemplate(TextType.TEXT);
        assertThat(optMessageTemplate).isEmpty();
    }

    @Test
    void testCreate() throws DomainException {
        Endpoint endpoint = Endpoint.create(TenantId.create("1000000001"), EndpointId.create("2000000001"),
                EndpointIdentifier.create("TEST"), "Test Endpoint");

        assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("2000000001"));
        assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("1000000001"));
        assertThat(endpoint.getEndpointIdentifier()).isEqualTo(EndpointIdentifier.create("TEST"));
        assertThat(endpoint.getEndpointName()).isEqualTo("Test Endpoint");
    }

    @Test
    void testCreateEndpointIdNull() {
        assertThrows(DomainException.class, () -> {
            Endpoint.create(TenantId.create("TEST"), null, EndpointIdentifier.create("TEST"), "Test Endpoint");
        }, "エンドポイントIDが未設定です");
    }

    @Test
    void testCreateTenantIdNull() {
        assertThrows(DomainException.class, () -> {
            Endpoint.create(null, EndpointId.create("ENDPOINT"), EndpointIdentifier.create("TEST"), "Test Endpoint");
        }, "テナントIDが未設定です");
    }

    @Test
    void testCreateEndpointNameNull() {
        assertThrows(DomainException.class, () -> {
            Endpoint.create(TenantId.create("TEST"), EndpointId.create("ENDPOINT"), EndpointIdentifier.create("TEST"),
                    null);
        }, "エンドポイント名が未設定です");
    }

    @Test
    void testCreateEndpointNameEmpty() {
        assertThrows(DomainException.class, () -> {
            Endpoint.create(TenantId.create("TEST"), EndpointId.create("ENDPOINT"), EndpointIdentifier.create("TEST"),
                    "");
        }, "エンドポイント名が未設定です");
    }

    @Test
    void testCreateEndpointNameMaxLength() {
        assertThrows(DomainException.class, () -> {
            Endpoint.create(TenantId.create("TEST"), EndpointId.create("ENDPOINT"), EndpointIdentifier.create("TEST"),
                    "123456789012345678901234567890123456789012345678901");
        }, "エンドポイント名は50文字以内である必要があります");
    }

}
