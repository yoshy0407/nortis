package org.nortis.test.user;

import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.security.core.context.SecurityContext;

/**
 * テストユーザのインタフェース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface TestUser {

    /**
     * {@link NortisUserDetails}を返します
     * 
     * @return {@link NortisUserDetails}
     */
    public NortisUserDetails getUserDetails();

    /**
     * {@link SecurityContext}に認証情報を設定します
     */
    public void setSecurityContext();
}
