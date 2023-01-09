package org.nortis.application.endpoint;

import org.nortis.domain.endpoint.EndpointEntityListener;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.EndpointRepositoryImpl;
import org.nortis.domain.tenant.TenantEntityListener;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRepositoryImpl;
import org.seasar.doma.jdbc.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class EndpointApplicationServiceTestConfig {

	@Bean
	TenantRepository tenantRepository(Config config) {
		return new TenantRepositoryImpl(config);
	}
	
	@Bean 
	TenantEntityListener tenantEntityListener(ApplicationEventPublisher applicationEventPublisher) {
		var listener = new TenantEntityListener();
		listener.setApplicationEventPublisher(applicationEventPublisher);
		return listener;
	}

	@Bean
	EndpointRepository endpointRepository(Config config) {
		return new EndpointRepositoryImpl(config);
	}
	
	@Bean
	EndpointApplicationService endpointApplicationService(
			TenantRepository tenantRepository, 
			EndpointRepository endpointRepository) {
		return new EndpointApplicationService(tenantRepository, endpointRepository);
	}
	
	@Bean
	EndpointEntityListener endpointEntityListener(ApplicationEventPublisher applicationEventPublisher) {
		var listener = new EndpointEntityListener();
		listener.setApplicationEventPublisher(applicationEventPublisher);
		return listener;
	}
	
}
