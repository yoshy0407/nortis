package org.nortis.domain.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.infrastructure.security.user.SuserNortisUser;
import org.nortis.infrastructure.security.user.TenantNortisUser;

/**
 * 認証に関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class AuthenticationDomainService {

    private final AuthenticationRepository authenticationRepository;

    private final TenantRepository tenantRepository;

    private final SuserRepository suserRepository;

    /** ユーザの認証の期限 */
    private final long expiredSecond;

    /**
     * 期限切れかどうかチェックします
     * 
     * @param authentication 認証
     * @param baseDatetime   基準日時
     * @return 期限切れかどうか
     */
    public boolean checkExpired(Authentication authentication, LocalDateTime baseDatetime) {
        LocalDateTime lastAccessDatetime = authentication.getLastAccessDatetime();
        // nullの場合、認証が行われていないため、期限切れではない
        if (lastAccessDatetime == null) {
            return false;
        }
        return isExpired(authentication, baseDatetime);
    }

    /**
     * 認可を実施します
     * 
     * @param apiKey APIキー
     * @return {@link NortisUserDetails}
     * @throws DomainException 認証失敗の場合
     */
    public NortisUserDetails authorize(ApiKey apiKey) throws DomainException {
        Optional<Authentication> optAuth = this.authenticationRepository.get(apiKey);
        if (optAuth.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("APIキー"));
        }
        Authentication authentication = optAuth.get();

        // テナントの場合は、期限切れ気にせずに認証を実施
        if (authentication.getTenantId() != null) {
            authentication.setLastAccessDatetime(LocalDateTime.now());
            authenticationRepository.update(authentication);

            Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(authentication.getTenantId());
            if (optTenant.isEmpty()) {
                // :TODO このメッセージはどうだろうか
                throw new DomainException(MessageCodes.nortis00003("テナントまたはユーザ"));
            }
            return TenantNortisUser.createOfTenant(authentication, optTenant.get(), false);
        }

        if (authentication.getUserId() != null) {
            authentication.setLastAccessDatetime(LocalDateTime.now());
            authenticationRepository.update(authentication);
            Optional<Suser> optUser = this.suserRepository.getByUserId(authentication.getUserId());
            if (optUser.isEmpty()) {
                // :TODO このメッセージはどうだろうか
                throw new DomainException(MessageCodes.nortis00003("テナントまたはユーザ"));
            }
            return SuserNortisUser.createOfUser(optUser.get(), authentication,
                    isExpired(authentication, LocalDateTime.now()));
        }

        throw new DomainException(MessageCodes.nortis60001());
    }

    private boolean isExpired(Authentication authentication, LocalDateTime baseDatetime) {
        if (authentication.getLastAccessDatetime() != null) {
            return baseDatetime.minusSeconds(expiredSecond).isAfter(authentication.getLastAccessDatetime());
        } else {
            return false;
        }
    }

}
