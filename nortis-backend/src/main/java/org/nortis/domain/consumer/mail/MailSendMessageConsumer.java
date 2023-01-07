package org.nortis.domain.consumer.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.Message;
import org.nortis.domain.consumer.MessageConsumer;
import org.nortis.domain.consumer.MessageFailureException;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * メールを送信するドメインサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class MailSendMessageConsumer implements MessageConsumer {

	/** メールコンシューマのリポジトリ */
	private final MailConsumerRepository mailConsumerRepository;
	
	/** メール送信コンポーネント */
	private final JavaMailSender javaMailSender;
	
	/** メール送信失敗時のハンドラ */
	private final MailSendFailureHandler mailSendFailureHandler;
	
	/** 送信元メールアドレス */
	private final String fromMailAddress;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void consume(Message message) throws MessageFailureException {
		List<MailConsumer> consumerList = 
				this.mailConsumerRepository.getFromEndpoint(message.getEndpointId());
		
		List<MimeMessage> messages = new ArrayList<>();
		
		for (MailConsumer mailConsumer : consumerList) {
			MimeMessage mimeMessage = createMessage(mailConsumer, message);
			messages.add(mimeMessage);
		}
		
		try {
			this.javaMailSender.send(messages.toArray(new MimeMessage[messages.size()]));
		} catch (MailException ex) {
			this.mailSendFailureHandler.handleSendError(ex);
			throw new MessageFailureException("メールの送信に失敗しました", ex);
		}
	}
	
	private MimeMessage createMessage(MailConsumer mailConsumer, Message message) throws MessageFailureException {
		MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(this.fromMailAddress);
			String[] addresses = mailConsumer.getMailList().stream()
					.map(data -> data.getMailAddress().toString())
					.toArray(i -> new String[i]);
			helper.setTo(addresses);
			helper.setSubject(message.getSubject());
			helper.setText(message.getMessageBody(), false);
		} catch (MessagingException ex) {
			this.mailSendFailureHandler.handleMessageError(ex);
			throw new MessageFailureException("メールのメッセージ構築に失敗しました", ex);	
		}
		return mimeMessage;
	}
	
}
