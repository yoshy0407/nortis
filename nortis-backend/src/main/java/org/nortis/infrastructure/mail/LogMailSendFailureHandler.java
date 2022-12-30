package org.nortis.infrastructure.mail;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LogMailSendFailureHandler implements MailSendFailureHandler {

	private final MessageSource messageSource;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessageError(MessagingException exception) {
		log.error(messageSource.getMessage("MSG90001", new Object[] {}, Locale.getDefault()), exception);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleSendError(MailException exception) {
		log.error(messageSource.getMessage("MSG90002", new Object[] {}, Locale.getDefault()), exception);		
	}

}
