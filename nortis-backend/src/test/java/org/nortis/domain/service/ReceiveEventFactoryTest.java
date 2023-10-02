package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;

class ReceiveEventFactoryTest extends TestBase {

    ReceiveEventFactory receiveEventFactory;

    @BeforeEach
    void setup() {
        this.receiveEventFactory = new ReceiveEventFactory(new ObjectMapper());
    }

    @Test
    void testCreateEvent() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");
        Map<String, Object> parameter = new LinkedHashMap<>();
        parameter.put("name", "Taro");
        parameter.put("age", "12");

        ReceiveEvent receiveEvent = this.receiveEventFactory.createEvent(tenantId, endpointId, parameter,
                Collections.emptyList());

        assertThat(receiveEvent.getEventId()).isNotNull();
        assertThat(receiveEvent.getTenantId()).isEqualTo(tenantId);
        assertThat(receiveEvent.getEndpointId()).isEqualTo(endpointId);
        assertThat(receiveEvent.getOccuredOn()).isNotNull();
        assertThat(receiveEvent.getParameterJson()).isEqualTo("{\"name\":\"Taro\",\"age\":\"12\"}");

    }

}
