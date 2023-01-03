package org.nortis.infrastructure.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.MailSendException;


class CompositeMailSendHandlerTest {

	CompositeMailSendHandler compositeHandler = new CompositeMailSendHandler();
	
	MailSendFailureHandler mockHandler1;

	MailSendFailureHandler mockHandler2;

	@BeforeEach
	void setup() {
		this.mockHandler1 = Mockito.mock(MailSendFailureHandler.class);
		this.compositeHandler.setHandler(mockHandler1);
		this.mockHandler2 = Mockito.mock(MailSendFailureHandler.class);
		this.compositeHandler.setHandler(mockHandler2);
	}
	
	@Test
	void testHandleMessageError() {
		this.compositeHandler.handleMessageError(new MessagingException("testError"));
		
		verify(mockHandler1).handleMessageError(any());
		verify(mockHandler2).handleMessageError(any());
	}

	@Test
	void testHandleSendError() {
		this.compositeHandler.handleSendError(new MailSendException("testError"));
		
		verify(mockHandler1).handleSendError(any());
		verify(mockHandler2).handleSendError(any());
	}

}
