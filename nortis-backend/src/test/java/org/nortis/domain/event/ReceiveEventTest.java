package org.nortis.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import test.BaseTestConfiguration;

@SpringBootTest(classes = {
		MessageSourceAutoConfiguration.class,
		BaseTestConfiguration.class
})
class ReceiveEventTest {

	@BeforeEach
	void setup() {
		ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		Mockito.when(applicationContext.getBean(eq(ObjectMapper.class)))
			.thenReturn(new ObjectMapper());
		
		ApplicationContextAccessor.set(applicationContext);
	}
	
	@Test
	void testSubscribed() {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("tenantId"), 
				EndpointId.create("endpoint1"), 
				"{ \"name\": \"Taro\", \"age\": \"12\" }");
		
		assertThat(receiveEvent.isSubscribed()).isFalse();
		receiveEvent.subscribe();
		assertThat(receiveEvent.isSubscribed()).isTrue();
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
