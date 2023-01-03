package org.nortis.infrastructure.mail;

import jakarta.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.mail.MailException;

/**
 * 集約する{@link MailSendFailureHandler}です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class CompositeMailSendHandler implements MailSendFailureHandler {

	/** 集約しているリスト */
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
	
	/**
	 * {@link MailSendFailureHandler}を追加します
	 * @param handler {@link MailSendFailureHandler}
	 */
	public void setHandler(MailSendFailureHandler handler) {
		this.composite.add(handler);
	}
	
}
