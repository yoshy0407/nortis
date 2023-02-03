package org.nortis.infrastructure.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * ロールを表す列挙型です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public enum Role {
	/**
	 * 管理者
	 */
	ADMIN,
	/**
	 * メンバー
	 */
	MEMBER;
	

	/**
	 * {@link GrantedAuthority}を返却します
	 * @return {@link GrantedAuthority}
	 */
	public GrantedAuthority getAuthority() {
		return new SimpleGrantedAuthority(this.name());
	}
	
}
