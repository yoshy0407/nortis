package org.nortis.infrastructure.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * メールに関するプロパティクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("nortis.mail")
public class NortisMailProperties {

	/** FROMに設定するメールアドレス */
	private String fromMailAddress;
	
}
