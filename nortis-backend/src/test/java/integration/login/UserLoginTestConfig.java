package integration.login;

import org.nortis.application.authentication.UserLoginService;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.AuthenticationRepositoryImpl;
import org.nortis.domain.service.PasswordDomainService;
import org.nortis.domain.user.SuserRepository;
import org.nortis.infrastructure.doma.repository.DomaSuserRepository;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * テスト用のコンフィグ
 * 
 * @author yoshiokahiroshi
 * @version
 */
@SuppressWarnings({ "deprecation" })
@Configuration
public class UserLoginTestConfig {

    @Bean
    SuserRepository suserRepository(Entityql entityql) {
        return new DomaSuserRepository(entityql);
    }

    @Bean
    AuthenticationRepository authenticationRepository(Config config) {
        return new AuthenticationRepositoryImpl(config);
    }

    @Bean
    PasswordDomainService passwordDomainService() {
        return new PasswordDomainService(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    UserLoginService userLoginService(SuserRepository suserRepository,
            AuthenticationRepository authenticationRepository, PasswordDomainService passwordDomainService) {
        return new UserLoginService(suserRepository, authenticationRepository, passwordDomainService);
    }

}
