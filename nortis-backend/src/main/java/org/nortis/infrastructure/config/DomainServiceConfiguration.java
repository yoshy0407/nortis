package org.nortis.infrastructure.config;

import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.mail.MailConsumerRepository;
import org.nortis.domain.consumer.mail.MailSendMessageConsumer;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.nortis.infrastructure.property.NortisMailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * ドメインサービスに関するコンフィグクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@EnableConfigurationProperties({
	NortisMailProperties.class
})
@Configuration(proxyBeanMethods = false)
public class DomainServiceConfiguration {

	/** メールに関するプロパティクラス */
	private final NortisMailProperties nortisMailProperties;

	/**
	 * {@link MailSendMessageConsumer}のBean
	 * @param mailConsumerRepository {@link MailConsumerDao}
	 * @param javaMailSender {@link JavaMailSender}
	 * @param mailSendFailureHandler {@link MailSendFailureHandler}
	 * @return {@link MailSendMessageConsumer}
	 */
	@Bean
	MailSendMessageConsumer mailSendDomainService(
			MailConsumerRepository mailConsumerRepository,
			JavaMailSender javaMailSender,
			MailSendFailureHandler mailSendFailureHandler) {
		return new MailSendMessageConsumer(
				mailConsumerRepository, 
				javaMailSender, 
				mailSendFailureHandler, 
				this.nortisMailProperties.getFromMailAddress());
	}
	
}
