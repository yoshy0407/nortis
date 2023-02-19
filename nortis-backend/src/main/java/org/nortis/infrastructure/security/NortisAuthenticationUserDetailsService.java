package org.nortis.infrastructure.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.authentication.AuthenticationApplicationService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * ユーザを認証する{@link AuthenticationUserDetailsService}です
 * @author yoshiokahiroshi
 * @version 1.0.o
 */
@Slf4j
@AllArgsConstructor
public class NortisAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	private final AuthenticationApplicationService authenticationApplicationService;
	
	private final MessageSource messageSource;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		try {
			return this.authenticationApplicationService.authenticateOf(token.getCredentials().toString());
		} catch (DomainException e) {
			String message = MessageCodes.nortis60001().resolveMessage(messageSource, LocaleContextHolder.getLocale());
			log.error(e.resolveMessage(messageSource));
			throw new UsernameNotFoundException(message, e);
		}
	}

}
