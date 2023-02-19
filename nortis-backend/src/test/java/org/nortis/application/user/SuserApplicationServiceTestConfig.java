package org.nortis.application.user;

import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.AuthenticationRepositoryImpl;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRepositoryImpl;
import org.nortis.domain.user.DomaSuserRepository;
import org.nortis.domain.user.SuserRepository;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.seasar.doma.jdbc.Config;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@TestConfiguration
class SuserApplicationServiceTestConfig implements ApplicationContextAware {

	@Bean
	AuthenticationRepository authenticationRepository(Config config) {
		return new AuthenticationRepositoryImpl(config);
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
		return new AuthenticationDomainService(authenticationRepository, suserRepository, passwordEncoder, 1800);
	}
	
	@Bean
	TenantRepository tenantRepository(Config config) {
		return new TenantRepositoryImpl(config);
	}
	
	@Bean
	TenantDomainService tenantDomainService(TenantRepository tenantRepository) {
		return new TenantDomainService(tenantRepository);
	}
	
	@Bean
	SuserApplicationService suserApplicationService(
			SuserRepository suserRepository,
			AuthenticationDomainService authenticationDomainService,
			TenantDomainService tenantDomainService) {
		return new SuserApplicationService(suserRepository, authenticationDomainService, tenantDomainService);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextAccessor.set(applicationContext);
	}
	
}
