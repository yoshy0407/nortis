package org.nortis.infrastructure.config;

import org.nortis.infrastructure.mail.CompositeMailSendHandler;
import org.nortis.infrastructure.mail.LogMailSendFailureHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MailSendhandlerConfiguration {

	@Bean
	public CompositeMailSendHandler mailSendFailureHandler(MessageSource messageSource) {
		CompositeMailSendHandler handler = new CompositeMailSendHandler();
		handler.setHandler(new LogMailSendFailureHandler(messageSource));
		return handler;
	}
	
}
