package org.nortis.domain.consumer.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.nortis.domain.consumer.Message;
import org.nortis.domain.consumer.MessageFailureException;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;


class MailSendMessageConsumerTest {

	MailConsumerRepository mailConsumerRepository;

	JavaMailSender javaMailSender;

	MailSendFailureHandler mailSendFailureHandler;

	MailSendMessageConsumer mailSendDomainService;

	@BeforeEach
	void setup() {
		this.mailConsumerRepository = Mockito.mock(MailConsumerRepository.class);
		this.javaMailSender = Mockito.mock(JavaMailSender.class);
		this.mailSendFailureHandler = Mockito.mock(MailSendFailureHandler.class);
		this.mailSendDomainService = new MailSendMessageConsumer(
				this.mailConsumerRepository, 
				this.javaMailSender,
				this.mailSendFailureHandler,
				"from@example.com");
	}

	@Test
	void testSend() throws MessagingException, IOException {

		List<MailConsumer> mailConsumerList = new ArrayList<>();
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				Lists.list(MailAddress.create("test1@example.com")), 
				"TEST_ID"));
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				Lists.list(MailAddress.create("test2@example.com")), 
				"TEST_ID"));

		Mockito.when(this.mailConsumerRepository.getFromEndpoint(eq(EndpointId.create("ENDPOINT1"))))
			.thenReturn(mailConsumerList);

		mockCreateMessage();

		Message message = new Message(
				TenantId.create("TENANT1"), 
				EndpointId.create("ENDPOINT1"), 
				"件名", 
				"本文");

		assertDoesNotThrow(() -> {
			this.mailSendDomainService.consume(message);			
		});

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
		List<MailConsumer> mailConsumerList = new ArrayList<>();
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				Lists.list(MailAddress.create("test1@example.com")), 
				"TEST_ID"));
		mailConsumerList.add(MailConsumer.create(
				EndpointId.create("ENDPOINT1"), 
				Lists.list(MailAddress.create("test2@example.com")), 
				"TEST_ID"));

		Mockito.when(
				this.mailConsumerRepository.getFromEndpoint(
						eq(EndpointId.create("ENDPOINT1"))))
		.thenReturn(mailConsumerList);

		mockCreateMessage();

		MailSendException ex = new MailSendException("error message");
		Mockito.doThrow(ex)
		.when(this.javaMailSender)
		.send(any(MimeMessage.class), any(MimeMessage.class));

		Message message = new Message(
				TenantId.create("TENANT1"), 
				EndpointId.create("ENDPOINT1"), 
				"件名", 
				"本文");


		assertThrows(MessageFailureException.class, () -> {
			this.mailSendDomainService.consume(message);
		}, "メールの送信に失敗しました");

		Mockito.verify(this.mailSendFailureHandler).handleSendError(eq(ex));
	}

	private void mockCreateMessage() {
		Mockito.when(
				this.javaMailSender.createMimeMessage()).thenAnswer(
						answer());
	}
	
	private Answer<MimeMessage> answer() {
		return new Answer<>() {
			@Override
			public MimeMessage answer(InvocationOnMock invocation) throws Throwable {
				return new MimeMessage((Session) null);
			}
		};
	}

}
