package org.nortis.application;

import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.EndpointRepositoryImpl;
import org.nortis.domain.mail.MailConsumerDomainService;
import org.nortis.domain.mail.MailConsumerRepository;
import org.nortis.domain.mail.MailConsumerRepositoryImpl;
import org.seasar.doma.jdbc.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MaillConsumerApplicationServiceTestConfig {

	@Bean
	MailConsumerRepository mailConsumerRepository(Config config) {
		return new MailConsumerRepositoryImpl(config);
	}
	
	@Bean
	EndpointRepository endpointRepository(Config config) {
		return new EndpointRepositoryImpl(config);
	}
	
	@Bean
	MailConsumerDomainService mailConsumerDomainService(EndpointRepository endpointRepository) {
		return new MailConsumerDomainService(endpointRepository);
	}
	
	@Bean
	MailConsumerApplicationService mailConsumerApplicationService(
			MailConsumerRepository mailConsumerRepository,
			MailConsumerDomainService mailConsumerDomainService) {
		return new MailConsumerApplicationService(mailConsumerRepository, mailConsumerDomainService);
	}
	
}
