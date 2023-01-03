package org.nortis.infrastructure.mail;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;


/**
 * ロギングを行う{@link MailSendFailureHandler}です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Slf4j
public class LogMailSendFailureHandler implements MailSendFailureHandler {

	/** {@link MessageSource} */
	private final MessageSource messageSource;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleMessageError(MessagingException exception) {
		if (log.isErrorEnabled()) {
			log.error(
					messageSource.getMessage(
							"MSG90001", 
							new Object[] {}, 
							LocaleContextHolder.getLocale()), 
					exception);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleSendError(MailException exception) {
		if (log.isErrorEnabled()) {
			log.error(
					messageSource.getMessage(
							"MSG90002", 
							new Object[] {}, 
							LocaleContextHolder.getLocale()),
					exception);					
		}
	}

}
