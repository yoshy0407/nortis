package org.nortis.infrastructure.config;

import org.nortis.infrastructure.doma.RestartClassLoaderClassHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Domaに関するコンフィグクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class DomaConfiguration {

	/**
	 * {@link RestartClassLoaderClassHelper}のBean
	 * @return {@link RestartClassLoaderClassHelper}のBean
	 */
	@Bean
	RestartClassLoaderClassHelper classHelper() {
		return new RestartClassLoaderClassHelper();
	}
	
}
