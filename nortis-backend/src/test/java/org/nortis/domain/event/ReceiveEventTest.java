package org.nortis.domain.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.value.Subscribed;
import org.nortis.domain.tenant.value.TenantId;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import test.BaseTestConfiguration;
import test.MockApplicationContextAccessor;

@SpringBootTest(classes = {
		MessageSourceAutoConfiguration.class,
		BaseTestConfiguration.class
})
class ReceiveEventTest {

	@BeforeEach
	void setup() {
		MockApplicationContextAccessor mockAccessor = new MockApplicationContextAccessor();
		mockAccessor.mockGetObjectMapper(new ObjectMapper());
	}
	
	@Test
	void testSubscribed() {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("tenantId"), 
				EndpointId.create("endpoint1"), 
				"{ \"name\": \"Taro\", \"age\": \"12\" }");
		
		assertThat(receiveEvent.getSubscribed()).isEqualTo(Subscribed.FALSE);
		receiveEvent.subscribe();
		assertThat(receiveEvent.getSubscribed()).isEqualTo(Subscribed.TRUE);
	}

	@Test
	void testCreate() {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("tenantId"), 
				EndpointId.create("endpoint1"), 
				"{ \"name\": \"Taro\", \"age\": \"12\" }");

		assertThat(receiveEvent.getEventId()).isNotNull();
		assertThat(receiveEvent.getTenantId()).isEqualTo(TenantId.create("tenantId"));
		assertThat(receiveEvent.getEndpointId()).isEqualTo(EndpointId.create("endpoint1"));
		assertThat(receiveEvent.getOccuredOn()).isNotNull();
		assertThat(receiveEvent.getTemplateParameter()).isEqualTo("{ \"name\": \"Taro\", \"age\": \"12\" }");
	}

}
