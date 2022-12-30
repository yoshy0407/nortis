package org.nortis.domain.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

class MailSendDomainServiceTest {

	MailConsumerRepository mailConsumerRepository;
	
	JavaMailSender javaMailSender;
	
	MailSendFailureHandler mailSendFailureHandler;
	
	MailSendDomainService mailSendDomainService;
	
	@BeforeEach
	void setup() {
		this.mailConsumerRepository = Mockito.mock(MailConsumerRepository.class);
		this.javaMailSender = Mockito.mock(JavaMailSender.class);
		this.mailSendFailureHandler = Mockito.mock(MailSendFailureHandler.class);
		this.mailSendDomainService = new MailSendDomainService(
				this.mailConsumerRepository, 
				this.javaMailSender,
				this.mailSendFailureHandler,
				"from@example.com");
	}
	
	@Test
	void testSend() throws MessagingException, IOException {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("TENANT1"), 
				EndpointId.create("ENDPOINT1"), 
				"件名", 
				"本文");
		
		List<MailConsumer> mailConsumerList = new ArrayList<>();
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				MailAddress.create("test1@example.com"), 
				"TEST_ID"));
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				MailAddress.create("test2@example.com"), 
				"TEST_ID"));
		
		Mockito.when(this.mailConsumerRepository.getFromEndpointId(eq(EndpointId.create("ENDPOINT1"))))
			.thenReturn(mailConsumerList);
		
		mockCreateMessage();
		
		boolean result = this.mailSendDomainService.send(receiveEvent);
		
		assertThat(result).isTrue();
		
		ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
		Mockito.verify(this.javaMailSender).send(captor.capture(), captor.capture());
		
		List<MimeMessage> messages = captor.getAllValues();
		
		assertThat(messages).hasSize(2);
		MimeMessage message1 = messages.get(0);
		assertThat(message1.getFrom()[0].toString()).isEqualTo("from@example.com");
		assertThat(message1.getRecipients(RecipientType.TO)[0].toString()).isEqualTo("test1@example.com");
		assertThat(message1.getSubject()).isEqualTo("件名");
		//assertThat(convert(message1.getInputStream())).isEqualTo("本文");

		MimeMessage message2 = messages.get(1);
		assertThat(message2.getFrom()[0].toString()).isEqualTo("from@example.com");
		assertThat(message2.getRecipients(RecipientType.TO)[0].toString()).isEqualTo("test2@example.com");
		assertThat(message2.getSubject()).isEqualTo("件名");
		//assertThat(convert(message2.getInputStream())).isEqualTo("本文");
	}
	
	@Test
	void testSendError() {
		ReceiveEvent receiveEvent = ReceiveEvent.create(
				TenantId.create("TENANT1"), 
				EndpointId.create("ENDPOINT1"), 
				"件名", 
				"本文");
		
		List<MailConsumer> mailConsumerList = new ArrayList<>();
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				MailAddress.create("test1@example.com"), 
				"TEST_ID"));
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				MailAddress.create("test2@example.com"), 
				"TEST_ID"));
		
		Mockito.when(this.mailConsumerRepository.getFromEndpointId(eq(EndpointId.create("ENDPOINT1"))))
			.thenReturn(mailConsumerList);

		mockCreateMessage();
		
		MailSendException ex = new MailSendException("error message");
		Mockito.doThrow(ex)
			.when(this.javaMailSender)
			.send(any(MimeMessage.class),any(MimeMessage.class));

		boolean result = this.mailSendDomainService.send(receiveEvent);
		
		assertThat(result).isFalse();
		
		Mockito.verify(this.mailSendFailureHandler).handleSendError(eq(ex));
	}
	
	private void mockCreateMessage() {
		Mockito.when(this.javaMailSender.createMimeMessage()).thenAnswer(new Answer<MimeMessage>() {

			@Override
			public MimeMessage answer(InvocationOnMock invocation) throws Throwable {
				return new MimeMessage((Session)null);
			}
		});
	}
	
}
