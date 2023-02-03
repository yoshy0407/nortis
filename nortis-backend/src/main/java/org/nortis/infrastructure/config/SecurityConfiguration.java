package org.nortis.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティに関する設定を行うインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
		
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
