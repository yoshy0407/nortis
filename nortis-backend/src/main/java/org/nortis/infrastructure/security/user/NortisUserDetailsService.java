package org.nortis.infrastructure.security.user;

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * ユーザを認証する{@link AuthenticationUserDetailsService}です
 * @author yoshiokahiroshi
 * @version 1.0.o
 */
public class NortisUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
