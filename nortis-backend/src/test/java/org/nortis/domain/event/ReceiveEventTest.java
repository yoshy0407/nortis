package org.nortis.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.value.Subscribed;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;

class ReceiveEventTest {

    @Test
    void testSubscribed() throws DomainException {
        ReceiveEvent receiveEvent = ReceiveEvent.create(TenantId.create("tenantId"), EndpointId.create("endpoint1"),
                "{ \"name\": \"Taro\", \"age\": \"12\" }", Collections.emptyList());

        assertThat(receiveEvent.getSubscribed()).isEqualTo(Subscribed.FALSE);
        receiveEvent.subscribe();
        assertThat(receiveEvent.getSubscribed()).isEqualTo(Subscribed.TRUE);
    }

    @Test
    void testCreate() throws DomainException {
        ReceiveEvent receiveEvent = ReceiveEvent.create(TenantId.create("tenantId"), EndpointId.create("endpoint1"),
                "{ \"name\": \"Taro\", \"age\": \"12\" }", Collections.emptyList());

        assertThat(receiveEvent.getEventId()).isNotNull();
        assertThat(receiveEvent.getTenantId()).isEqualTo(TenantId.create("tenantId"));
        assertThat(receiveEvent.getEndpointId()).isEqualTo(EndpointId.create("endpoint1"));
        assertThat(receiveEvent.getOccuredOn()).isNotNull();
        assertThat(receiveEvent.getParameterJson()).isEqualTo("{ \"name\": \"Taro\", \"age\": \"12\" }");
    }

}
