package org.nortis.infrastructure.config;

import org.nortis.infrastructure.doma.RestartClassLoaderClassHelper;
import org.seasar.doma.boot.TryLookupEntityListenerProvider;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.springframework.context.annotation.Bean;

public class DomaConfiguration {

	@Bean
	RestartClassLoaderClassHelper classHelper() {
		return new RestartClassLoaderClassHelper();
	}
	
	@Bean
	EntityListenerProvider entityListenerProvider() {
		return new TryLookupEntityListenerProvider();
	}
	
}
