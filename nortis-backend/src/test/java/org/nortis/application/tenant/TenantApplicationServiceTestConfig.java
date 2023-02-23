package org.nortis.application.tenant;

import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.AuthenticationRepositoryImpl;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantEntityListener;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRepositoryImpl;
import org.nortis.domain.user.AuthorityCheckDomainService;
import org.nortis.domain.user.DomaSuserRepository;
import org.nortis.domain.user.SuserRepository;
import org.seasar.doma.jdbc.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
@SuppressWarnings("deprecation")
class TenantApplicationServiceTestConfig {

	@Bean
	TenantRepository tenantRepository(Config config) {
		return new TenantRepositoryImpl(config);
	}
	
	@Bean
	AuthenticationRepository authenticationRepository(Config config) {
		return new AuthenticationRepositoryImpl(config);
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
	SuserRepository suserRepository(Config config) {
		return new DomaSuserRepository(config);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	AuthenticationDomainService authenticationDomainService(
			AuthenticationRepository authenticationRepository,
			SuserRepository suserRepository,
			PasswordEncoder passwordEncoder) {
		return new AuthenticationDomainService(
				authenticationRepository,
				suserRepository,
				passwordEncoder,
				3600);
	}
	
	@Bean
	AuthorityCheckDomainService authorityCheckDomainService(SuserRepository suserRepository) {
		return new AuthorityCheckDomainService(suserRepository);
	}
	
	@Bean
	TenantApplicationService tenantApplicationService(
			TenantRepository tenantRepository, 
			TenantDomainService tenantDomainService,
			AuthenticationDomainService authenticationDomainService,
			AuthorityCheckDomainService authorityCheckDomainService) {
		return new TenantApplicationService(tenantRepository, tenantDomainService, 
				authenticationDomainService, authorityCheckDomainService);
	}
	
}
