package org.nortis.domain.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;

class ReceiveEventTest {

	@Test
	void testSubscribed() {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("tenantId"), 
				EndpointId.create("endpoint1"), 
				"subject", 
				"message");
		
		assertThat(receiveEvent.isSubscribed()).isFalse();
		receiveEvent.subscribe();
		assertThat(receiveEvent.isSubscribed()).isTrue();
	}

	@Test
	void testCreate() {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("tenantId"), 
				EndpointId.create("endpoint1"), 
				"subject", 
				"message");

		assertThat(receiveEvent.getEventId()).isNotNull();
		assertThat(receiveEvent.getTenantId()).isEqualTo(TenantId.create("tenantId"));
		assertThat(receiveEvent.getEndpointId()).isEqualTo(EndpointId.create("endpoint1"));
		assertThat(receiveEvent.getOccuredOn()).isNotNull();
		assertThat(receiveEvent.getSubject()).isEqualTo("subject");
		assertThat(receiveEvent.getMessageBody()).isEqualTo("message");
	}

}
