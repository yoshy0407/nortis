package org.nortis.infrastructure.mail;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.infrastructure.message.MessageCode;
import org.nortis.infrastructure.message.MessageCodes;
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
			MessageCode code = MessageCodes.nortis90001();
			log.error(
					messageSource.getMessage(
							code.getCode(), 
							code.getArgs(),
							code.getDefaultMessage(),
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
			MessageCode code = MessageCodes.nortis90002();
			log.error(
					messageSource.getMessage(
							code.getCode(), 
							code.getArgs(),
							code.getDefaultMessage(),
							LocaleContextHolder.getLocale()),
					exception);					
		}
	}

}
