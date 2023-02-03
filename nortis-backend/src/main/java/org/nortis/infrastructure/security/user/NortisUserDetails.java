package org.nortis.infrastructure.security.user;

import org.springframework.security.core.userdetails.UserDetails;


/**
 * このアプリケーションで利用する{@link UserDetails}の拡張インタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface NortisUserDetails extends UserDetails {

	/**
	 * テナントIDを返却します
	 * @return テナントID
	 */
	public String[] getTenantId();
		
}
