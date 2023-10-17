package org.nortis.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 認証に関するプロパティです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "nortis.authentication")
public class AuthenticationProperties {

	/** セッションの有効期間（秒） */
	private long sessionExpireSecond = 3600L;
	
}
