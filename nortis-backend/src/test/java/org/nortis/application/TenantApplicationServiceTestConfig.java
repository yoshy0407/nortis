package org.nortis.application;

import org.nortis.application.tenant.TenantApplicationService;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantEntityListener;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRepositoryImpl;
import org.seasar.doma.jdbc.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class TenantApplicationServiceTestConfig {

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
	TenantDomainService tenantDomainService(TenantRepository tenantRepository) {
		return new TenantDomainService(tenantRepository);
	}
	
	@Bean
	TenantApplicationService tenantApplicationService(TenantRepository tenantRepository, TenantDomainService tenantDomainService) {
		return new TenantApplicationService(tenantRepository, tenantDomainService);
	}
	
}
