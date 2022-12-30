package org.nortis.infrastructure.config;

import org.nortis.domain.mail.MailConsumerRepository;
import org.nortis.domain.mail.MailSendDomainService;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.nortis.infrastructure.property.NortisMailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableConfigurationProperties({
	NortisMailProperties.class
})
public class DomainServiceConfiguration {

	private final NortisMailProperties nortisMailProperties;

	@Bean
	MailSendDomainService mailSendDomainService(
			MailConsumerRepository mailConsumerRepository,
			JavaMailSender javaMailSender,
			MailSendFailureHandler mailSendFailureHandler) {
		return new MailSendDomainService(mailConsumerRepository, javaMailSender, mailSendFailureHandler, this.nortisMailProperties.getFromMailAddress());
	}
	
}
