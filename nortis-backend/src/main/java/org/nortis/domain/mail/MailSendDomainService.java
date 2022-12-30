package org.nortis.domain.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.nortis.domain.event.ReceiveEvent;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailSendDomainService {

	private final MailConsumerRepository mailConsumerRepository;
	
	private final JavaMailSender javaMailSender;
	
	private final MailSendFailureHandler mailSendFailureHandler;
	
	private final String fromMailAddress;
	
	public boolean send(ReceiveEvent receiveEvent) {
		List<MailConsumer> consumerList = this.mailConsumerRepository.getFromEndpointId(receiveEvent.getEndpointId());
		
		boolean result = true;
		List<MimeMessage> messages = new ArrayList<>();
		
		for (MailConsumer mailConsumer : consumerList) {
			Optional<MimeMessage> message = createMessage(mailConsumer, receiveEvent);
			if (message.isPresent()) {
				messages.add(message.get());
			} else {
				result = false;
			}
		}
		
		if (!result) {
			return result;
		}
		
		try {
			this.javaMailSender.send(messages.toArray(new MimeMessage[messages.size()]));
		} catch (MailException ex) {
			this.mailSendFailureHandler.handleSendError(ex);
			return false;
		}
		return true;
	}
	
	private Optional<MimeMessage> createMessage(MailConsumer mailConsumer, ReceiveEvent receiveEvent) {
		MimeMessage message = this.javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(this.fromMailAddress);
			helper.setTo(mailConsumer.getMailAddress().toString());
			helper.setSubject(receiveEvent.getSubject());
			helper.setText(receiveEvent.getMessageBody(), false);
		} catch (MessagingException ex) {
			this.mailSendFailureHandler.handleMessageError(ex);
			return Optional.empty();
		}
		return Optional.of(message);
	}
	
}
