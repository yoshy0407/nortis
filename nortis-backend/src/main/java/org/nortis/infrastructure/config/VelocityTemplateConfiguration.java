package org.nortis.infrastructure.config;

import org.apache.velocity.app.VelocityEngine;
import org.nortis.infrastructure.template.VelocityTemplateRender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * テンプレートに関する設定クラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration
public class VelocityTemplateConfiguration {

	@Bean
	VelocityEngine velocityEngine() {
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		return ve;
	}
	
	@Bean
	VelocityTemplateRender templateRender(VelocityEngine velocityEngine) {
		return new VelocityTemplateRender(velocityEngine);
	}
	
}
