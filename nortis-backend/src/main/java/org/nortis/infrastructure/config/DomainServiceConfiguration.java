package org.nortis.infrastructure.config;

import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.service.AuthenticationDomainService;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.user.SuserRepository;
import org.nortis.infrastructure.property.NortisMailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ドメインサービスに関するコンフィグクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@EnableConfigurationProperties({ NortisMailProperties.class, AuthenticationProperties.class })
@Configuration(proxyBeanMethods = false)
public class DomainServiceConfiguration {

    /** メールに関するプロパティクラス */
    private final AuthenticationProperties authenticationProperties;

    @Bean
    AuthenticationDomainService authenticationDomainService(AuthenticationRepository authenticationRepository,
            TenantRepository tenantRepository, SuserRepository suserRepository, PasswordEncoder passwordEncoder) {
        return new AuthenticationDomainService(authenticationRepository, tenantRepository, suserRepository,
                authenticationProperties.getSessionExpireSecond());
    }

}
