package org.nortis.infrastructure.doma.repository;

import org.nortis.domain.tenant.TenantEntityListener;
import org.nortis.domain.tenant.TenantRoleEntityListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * テスト用のコンフィグクラス
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Configuration
public class DomaTenantRepositoryTestConfig {

    @Bean
    TenantEntityListener tenantEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        var tenantEntityListener = new TenantEntityListener();
        tenantEntityListener.setApplicationEventPublisher(applicationEventPublisher);
        return tenantEntityListener;
    }

    @Bean
    TenantRoleEntityListener tenantRoleEntityListener(ApplicationEventPublisher applicationEventPublisher) {
        var listener = new TenantRoleEntityListener();
        listener.setApplicationEventPublisher(applicationEventPublisher);
        return listener;
    }

}
