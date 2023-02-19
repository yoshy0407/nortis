package org.nortis.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nortis.application.authentication.AuthenticationApplicationService;
import org.nortis.infrastructure.security.NortisAuthenticaitonFailureHandler;
import org.nortis.infrastructure.security.NortisAuthenticationUserDetailsService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

/**
 * セキュリティに関する設定を行うインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration
public class SecurityConfiguration {

	@Bean
	AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService(
			AuthenticationApplicationService authenticationApplicationService,
			MessageSource messageSource) {
		return new NortisAuthenticationUserDetailsService(authenticationApplicationService, messageSource);
	}
	
	@Bean
	PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(
			AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsService) {
		var provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	AuthenticationFailureHandler authenticationFailureHandler(MessageSource messageSource, ObjectMapper objectMapper) {
		return new NortisAuthenticaitonFailureHandler(messageSource, objectMapper);
	}
	
	@Bean
	SecurityFilterChain securityFilter(
			HttpSecurity http, 
			AuthenticationFailureHandler authenticationFailureHandler) throws Exception {
		RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
		filter.setCredentialsRequestHeader("X-NORTIS-APIKEY");
		filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		filter.setAuthenticationFailureHandler(authenticationFailureHandler);
		
		return http.addFilter(filter)
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers("/login", "/logout").permitAll();
				auth.anyRequest().authenticated();
			})
			.csrf(csrf -> {
				csrf.disable();
			})
			.sessionManagement(session -> {
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			}).build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
