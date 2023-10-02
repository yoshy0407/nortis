package org.nortis.infrastructure.doma.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.MessageTemplate;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
  "/META-INF/ddl/dropEndpoint.sql",
  "/ddl/createEndpoint.sql",
  "/META-INF/data/domain/del_ins_endpoint.sql"
})
//@formatter:on
@ContextConfiguration(classes = { DomaEndpointRepositoryTestConfig.class })
class DomaEndpointRepositoryTest extends RepositoryTestBase {

    @Autowired
    Entityql entityql;

    DomaEndpointRepository repository;

    @BeforeEach
    void setup() {
        this.repository = new DomaEndpointRepository(entityql);
    }

    @Test
    void testGet() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Optional<Endpoint> optEndpoint = this.repository.get(tenantId, endpointId);

        assertThat(optEndpoint).isPresent();

        Endpoint endpoint = optEndpoint.get();
        assertThat(endpoint.getTenantId()).isEqualTo(tenantId);
        assertThat(endpoint.getEndpointId()).isEqualTo(endpointId);
        assertThat(endpoint.getEndpointIdentifier()).isEqualTo(EndpointIdentifier.create("ENDPOINT1"));
        assertThat(endpoint.getEndpointName()).isEqualTo("テストエンドポイント１");

        assertThat(endpoint.getMessageTemplateList()).hasSize(2);

        Optional<MessageTemplate> optTemplate1 = endpoint.getTemplate(TextType.HTML);

        assertThat(optTemplate1).isPresent();
        MessageTemplate template1 = optTemplate1.get();
        assertThat(template1.getEndpointId()).isEqualTo(endpointId);
        assertThat(template1.getTextType()).isEqualTo(TextType.HTML);
        assertThat(template1.getSubjectTemplate()).isEqualTo(SubjectTemplate.create("subject"));
        assertThat(template1.getBodyTemplate()).isEqualTo(BodyTemplate.create("<p>test</p>"));

        Optional<MessageTemplate> optTemplate2 = endpoint.getTemplate(TextType.TEXT);

        assertThat(optTemplate2).isPresent();
        MessageTemplate template2 = optTemplate2.get();
        assertThat(template2.getEndpointId()).isEqualTo(endpointId);
        assertThat(template2.getTextType()).isEqualTo(TextType.TEXT);
        assertThat(template2.getSubjectTemplate()).isEqualTo(SubjectTemplate.create("subject"));
        assertThat(template2.getBodyTemplate()).isEqualTo(BodyTemplate.create("body"));

    }

    @Test
    void testGetByEndpointIdentifier() throws DomainException {
        TenantId tenantId = TenantId.create("1000000002");
        EndpointIdentifier endpointIdentifier = EndpointIdentifier.create("ENDPOINT3");

        Optional<Endpoint> optEndpoint = this.repository.getByEndpointIdentifier(tenantId, endpointIdentifier);

        assertThat(optEndpoint).isPresent();

        Endpoint endpoint = optEndpoint.get();
        assertThat(endpoint.getTenantId()).isEqualTo(tenantId);
        assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("2000000003"));
        assertThat(endpoint.getEndpointIdentifier()).isEqualTo(endpointIdentifier);
        assertThat(endpoint.getEndpointName()).isEqualTo("テストエンドポイント３");
    }

    @Test
    void testGetFromTenantId() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");

        List<Endpoint> endpointList = this.repository.getFromTenantId(tenantId);

        assertThat(endpointList).hasSize(2);
        assertThat(endpointList.get(0).getEndpointId()).isEqualTo(EndpointId.create("2000000001"));
        assertThat(endpointList.get(1).getEndpointId()).isEqualTo(EndpointId.create("2000000002"));
    }

    @Test
    void testSave() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000099");
        EndpointIdentifier endpointIdentifier = EndpointIdentifier.create("ENDPOINT_TEST");

        Endpoint endpoint = Endpoint.create(tenantId, endpointId, endpointIdentifier, "テストエンドポイント");
        endpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("subject"), BodyTemplate.create("message"));

        this.repository.save(endpoint);

        Optional<Endpoint> optEndpoint = this.repository.get(tenantId, endpointId);

        assertThat(optEndpoint).isPresent();

        Endpoint result = optEndpoint.get();
        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getEndpointId()).isEqualTo(endpointId);
        assertThat(result.getEndpointIdentifier()).isEqualTo(EndpointIdentifier.create("ENDPOINT_TEST"));
        assertThat(result.getEndpointName()).isEqualTo("テストエンドポイント");

        assertThat(result.getMessageTemplateList()).hasSize(1);

        Optional<MessageTemplate> optTemplate1 = result.getTemplate(TextType.TEXT);

        assertThat(optTemplate1).isPresent();
        MessageTemplate template1 = optTemplate1.get();
        assertThat(template1.getEndpointId()).isEqualTo(endpointId);
        assertThat(template1.getTextType()).isEqualTo(TextType.TEXT);
        assertThat(template1.getSubjectTemplate()).isEqualTo(SubjectTemplate.create("subject"));
        assertThat(template1.getBodyTemplate()).isEqualTo(BodyTemplate.create("message"));

    }

    @Test
    void testUpdate_UpdateDelete() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Optional<Endpoint> optEndpoint = this.repository.get(tenantId, endpointId);

        Endpoint endpoint = optEndpoint.get();
        // EndpointのUpdate
        endpoint.changeEndpointName("テスト");
        // MessageTemplateの更新
        endpoint.getTemplate(TextType.TEXT).get().updateTemplates(SubjectTemplate.create("subject_test"),
                BodyTemplate.create("body_test"));
        // MessageTemplateの削除
        endpoint.deleteTemplate(TextType.HTML);

        this.repository.update(endpoint);

        Optional<Endpoint> optResult = this.repository.get(tenantId, endpointId);

        assertThat(optResult).isPresent();

        Endpoint result = optResult.get();
        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getEndpointId()).isEqualTo(endpointId);
        assertThat(result.getEndpointIdentifier()).isEqualTo(EndpointIdentifier.create("ENDPOINT1"));
        assertThat(result.getEndpointName()).isEqualTo("テスト");

        assertThat(result.getMessageTemplateList()).hasSize(1);

        Optional<MessageTemplate> optTemplate1 = result.getTemplate(TextType.TEXT);

        assertThat(optTemplate1).isPresent();
        MessageTemplate template1 = optTemplate1.get();
        assertThat(template1.getEndpointId()).isEqualTo(endpointId);
        assertThat(template1.getTextType()).isEqualTo(TextType.TEXT);
        assertThat(template1.getSubjectTemplate()).isEqualTo(SubjectTemplate.create("subject_test"));
        assertThat(template1.getBodyTemplate()).isEqualTo(BodyTemplate.create("body_test"));
    }

    @Test
    void testUpdate_Insert() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000002");

        Optional<Endpoint> optEndpoint = this.repository.get(tenantId, endpointId);

        Endpoint endpoint = optEndpoint.get();
        // EndpointのUpdate
        endpoint.changeEndpointName("テスト");
        // MessageTemplateの追加
        endpoint.createTemplate(TextType.HTML, SubjectTemplate.create("subject"),
                BodyTemplate.create("<p>message</p>"));

        this.repository.update(endpoint);

        Optional<Endpoint> optResult = this.repository.get(tenantId, endpointId);

        assertThat(optResult).isPresent();

        Endpoint result = optResult.get();
        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getEndpointId()).isEqualTo(endpointId);
        assertThat(result.getEndpointIdentifier()).isEqualTo(EndpointIdentifier.create("ENDPOINT2"));
        assertThat(result.getEndpointName()).isEqualTo("テスト");

        assertThat(result.getMessageTemplateList()).hasSize(2);

    }

    @Test
    void testRemove() throws DomainException {
        TenantId tenantId = TenantId.create("1000000002");
        EndpointId endpointId = EndpointId.create("2000000003");

        Optional<Endpoint> optEndpoint = this.repository.get(tenantId, endpointId);

        this.repository.remove(optEndpoint.get());

        Optional<Endpoint> endpointResult = this.repository.get(tenantId, endpointId);
        assertThat(endpointResult).isEmpty();
    }

    @Test
    void testRemoveAll() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");

        List<Endpoint> endpointList = this.repository.getFromTenantId(tenantId);

        this.repository.removeAll(endpointList);

        List<Endpoint> actualList = this.repository.getFromTenantId(tenantId);
        assertThat(actualList).isEmpty();
    }

}
