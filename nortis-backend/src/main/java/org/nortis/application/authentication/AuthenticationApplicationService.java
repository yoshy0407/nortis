package org.nortis.application.authentication;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.service.AuthenticationDomainService;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 認証に関するアプリケーションサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class AuthenticationApplicationService {

    private final AuthenticationRepository authenticationRepository;

    private final AuthenticationDomainService authenticationDomainService;

    /**
     * APIキーによる認可を実施します
     * 
     * @param apiKey APIキー
     * @return APIキーに紐づく{@link UserDetails}
     * @throws DomainException ドメインロジックエラー
     */
    public NortisUserDetails authenticateOf(String apiKey) throws DomainException {
        return this.authenticationDomainService.authorize(ApiKey.create(apiKey));
    }

    /**
     * 期限切れの認証を削除します
     * 
     * @param baseDatetime 基準日時
     */
    public void removeExpiredAuthentication(LocalDateTime baseDatetime) {
        List<Authentication> userAuthentications = this.authenticationRepository.getUserAuthentication();
        for (Authentication authentication : userAuthentications) {
            if (this.authenticationDomainService.checkExpired(authentication, baseDatetime)) {
                this.authenticationRepository.remove(authentication);
            }
        }
    }
}
