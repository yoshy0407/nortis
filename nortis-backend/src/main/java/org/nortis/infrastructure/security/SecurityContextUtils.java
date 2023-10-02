package org.nortis.infrastructure.security;

import java.util.Optional;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * {@link SecurityContext}に関するユーティリティクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class SecurityContextUtils {

    /**
     * {@link SecurityContext}を取得します
     * 
     * @return {@link SecurityContext}
     */
    public static SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    /**
     * 認証情報を返却します
     * 
     * @return 認証情報
     */
    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(getContext().getAuthentication());
    }

    /**
     * {@link NortisUserDetails}を取得します
     * 
     * @return {@link NortisUserDetails}
     */
    public static Optional<NortisUserDetails> getUserDetails() {
        Optional<Authentication> authentication = getAuthentication();
        return authentication.isPresent() ? Optional.ofNullable((NortisUserDetails) authentication.get().getPrincipal())
                : Optional.empty();
    }

    /**
     * 現在の認証ユーザIDを取得します
     * 
     * @return 認証ユーザID
     */
    public static Identity getCurrentAuthorizedId() {
        return SecurityContextUtils.getUserDetails().map(user -> user.getIdentity()).orElse(createDummyIdentity());
    }

    /**
     * 認証情報を設定します
     * 
     * @param authentication 認証情報
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContext securityContext = getContext();
        securityContext.setAuthentication(authentication);
    }

    private static Identity createDummyIdentity() {
        try {
            return new Identity("9999999999", "ダミーID");
        } catch (DomainException ex) {
            UnexpectedException.convertDomainException(ex);
        }
        return null;
    }

}
