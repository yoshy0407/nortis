package org.nortis.infrastructure.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailException;

import jakarta.mail.MessagingException;

public class CompositeMailSendHandler implements MailSendFailureHandler {

	private final List<MailSendFailureHandler> composite = new ArrayList<>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessageError(MessagingException exception) {
		this.composite.forEach(handler -> handler.handleMessageError(exception));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleSendError(MailException exception) {
		this.composite.forEach(handler -> handler.handleSendError(exception));
	}
	
	public void setHandler(MailSendFailureHandler handler) {
		this.composite.add(handler);
	}

}
