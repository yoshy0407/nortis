package org.nortis.infrastructure.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("nortis.mail")
public class NortisMailProperties {

	private String fromMailAddress;
	
}
