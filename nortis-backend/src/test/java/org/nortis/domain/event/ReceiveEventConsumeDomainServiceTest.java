package org.nortis.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.nortis.domain.consumer.ConsumeFailureException;
import org.nortis.domain.consumer.Message;
import org.nortis.domain.consumer.MessageConsumer;
import org.nortis.domain.consumer.MessageFactory;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import test.MockApplicationContextAccessor;

class ReceiveEventConsumeDomainServiceTest {

	ReceiveEventConsumeDomainService domainService;
	
	EndpointRepository endpointRepository;
	
	MessageConsumer messageConsumer1;

	MessageConsumer messageConsumer2;
	
	MessageFactory messageFactory;

	@BeforeEach
	void setup() {
		endpointRepository = Mockito.mock(EndpointRepository.class);
		messageConsumer1 = Mockito.mock(MessageConsumer.class);
		messageConsumer2 = Mockito.mock(MessageConsumer.class);
		messageFactory = Mockito.mock(MessageFactory.class);
		
		MockApplicationContextAccessor mockAccessor = new MockApplicationContextAccessor();
		mockAccessor.mockGetObjectMapper(new ObjectMapper());
		
		this.domainService = new ReceiveEventConsumeDomainService(
				endpointRepository, 
				Lists.list(messageConsumer1, messageConsumer2), 
				messageFactory);
	}
	
	@Test
	void testConsumeEvent() throws ConsumeFailureException {
		Endpoint endpoint = Endpoint.create(
				EndpointId.create("TEST1"), 
				TenantId.create("TENANT1"), 
				"エンドポイント１", 
				"subject", 
				"messageBody", 
				"TEST_ID");
		
		Mockito.when(this.endpointRepository.get(
				Mockito.eq(TenantId.create("TENANT1")), 
				Mockito.eq(EndpointId.create("TEST1"))))
			.thenReturn(Optional.of(endpoint));
		
		Mockito.when(this.messageFactory.createMessage(any(), any()))
			.thenReturn(new Message(TenantId.create("TENANT1"), EndpointId.create("TETS1"), "subject", "messageBody"));
		
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("TENANT1"), 
				EndpointId.create("TEST1"), 
				"{ \"name\": \"John\" }");
		
		assertDoesNotThrow(() -> {
			this.domainService.consumeEvent(receiveEvent);			
		});
		
		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		Mockito.verify(messageConsumer1).consume(captor.capture());
		Mockito.verify(messageConsumer2).consume(captor.capture());
		
		List<Message> messages = captor.getAllValues();
		assertThat(messages).hasSize(2);
	}

}
