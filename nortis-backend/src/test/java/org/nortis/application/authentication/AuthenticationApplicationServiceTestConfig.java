package org.nortis.application.authentication;

import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.AuthenticationRepositoryImpl;
import org.nortis.domain.user.DomaSuserRepository;
import org.nortis.domain.user.SuserRepository;
import org.seasar.doma.jdbc.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@TestConfiguration
class AuthenticationApplicationServiceTestConfig {

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
		return new AuthenticationDomainService(
				authenticationRepository, suserRepository, passwordEncoder, 1800);
	}
	
	@Bean
	AuthenticationApplicationService authenticationApplicationService(
			AuthenticationRepository authenticationRepository,
			AuthenticationDomainService authenticationDomainService) {
		return new AuthenticationApplicationService(authenticationRepository, authenticationDomainService);
	}


}
