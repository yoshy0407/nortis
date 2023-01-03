package org.nortis.infrastructure.config;

import org.nortis.infrastructure.mail.CompositeMailSendHandler;
import org.nortis.infrastructure.mail.LogMailSendFailureHandler;
import org.nortis.infrastructure.mail.MailSendFailureHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link MailSendFailureHandler}に関するコンフィグクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class MailSendhandlerConfiguration {

	/**
	 * {@link CompositeMailSendHandler}のBean
	 * @param messageSource {@link MessageSource}
	 * @return {@link CompositeMailSendHandler}
	 */
	@Bean
	CompositeMailSendHandler mailSendFailureHandler(MessageSource messageSource) {
		CompositeMailSendHandler handler = new CompositeMailSendHandler();
		handler.setHandler(new LogMailSendFailureHandler(messageSource));
		return handler;
	}
	
}
