package org.nortis.domain.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.template.VelocityTemplateRender;
import test.MockApplicationContextAccessor;


class MessageFactoryTest {

	MessageFactory messageFactory;
	
	@BeforeEach
	void setup() {
		ObjectMapper objectMapper = new ObjectMapper();
		MockApplicationContextAccessor mockAccessor = new MockApplicationContextAccessor();
		mockAccessor.mockGetObjectMapper(objectMapper);
		
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		VelocityTemplateRender templateRender = new VelocityTemplateRender(ve);
		this.messageFactory = new MessageFactory(templateRender, new ObjectMapper());
	}
	
	@Test
	void testCreateMessage() {
		Endpoint endpoint = Endpoint.create(
				EndpointId.create("TEST1"), 
				TenantId.create("TENANT1"), 
				"TEST ENDPOINT", 
				"hello ${name}", 
				"message: hello ${name}", 
				"TEST_ID");
		
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("TENANT1"), 
				EndpointId.create("TEST1"), 
				"{ \"name\": \"John\" }");
		
		Message message = this.messageFactory.createMessage(endpoint, receiveEvent);
		
		assertThat(message.getTenantId()).isEqualTo(TenantId.create("TENANT1"));
		assertThat(message.getEndpointId()).isEqualTo(EndpointId.create("TEST1"));
		assertThat(message.getSubject()).isEqualTo("hello John");
		assertThat(message.getMessageBody()).isEqualTo("message: hello John");
	}

}
