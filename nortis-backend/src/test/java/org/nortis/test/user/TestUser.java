package org.nortis.test.user;

import org.nortis.infrastructure.security.user.NortisUserDetails;

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

}
