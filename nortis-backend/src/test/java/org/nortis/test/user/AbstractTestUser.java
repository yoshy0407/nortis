package org.nortis.test.user;

import org.nortis.infrastructure.security.SecurityContextUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * 抽象的な{@link TestUser}
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public abstract class AbstractTestUser implements TestUser {

    @Override
    public void setSecurityContext() {
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(getUserDetails(),
                null);
        SecurityContextUtils.setAuthentication(authentication);

    }

}
