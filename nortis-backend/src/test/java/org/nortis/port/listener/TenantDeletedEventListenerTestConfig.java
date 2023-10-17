package org.nortis.port.listener;

import org.nortis.application.endpoint.EndpointApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * テスト用のコンフィグ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration
public class TenantDeletedEventListenerTestConfig {

    @Bean
    EndpointEventListener tenantDeletedEventListener(EndpointApplicationService endpointApplicationService) {
        return new EndpointEventListener(endpointApplicationService);
    }
}
