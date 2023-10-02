package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nortis.TestBase;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.template.TemplateRender;

@ExtendWith(MockitoExtension.class)
class EndpointDomainServiceTest extends TestBase {

    @Mock
    TemplateRender templateRender;

    @Mock
    ReceiveEventFactory receiveEventFactory;

    EndpointDomainService domainService;

    @BeforeEach
    void setup() {
        this.domainService = new EndpointDomainService(this.templateRender, this.receiveEventFactory);
    }

    @Test
    void testReceiveEndpoint() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");
        EndpointIdentifier endpointIdentifier = EndpointIdentifier.create("TEST");

        Endpoint endpoint = Endpoint.create(tenantId, endpointId, endpointIdentifier, "テスト");
        endpoint.createTemplate(TextType.TEXT, SubjectTemplate.create("subject"), BodyTemplate.create("body"));
        endpoint.createTemplate(TextType.HTML, SubjectTemplate.create("subject"), BodyTemplate.create("<p>body</p>"));

        ReceiveEvent testDataReceiveEvent = ReceiveEvent.create(tenantId, endpointId, "", Collections.emptyList());
        when(this.receiveEventFactory.createEvent(eq(tenantId), eq(endpointId), eq(Collections.emptyMap()), anyList()))
                .thenReturn(testDataReceiveEvent);

        ReceiveEvent receiveEvent = this.domainService.receiveEndpoint(endpoint, Collections.emptyMap());

        // インスタンスが同じであることを確認
        assertThat(receiveEvent).isEqualTo(testDataReceiveEvent);
    }

}
