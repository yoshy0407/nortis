package org.nortis.test.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.Tenant;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.infrastructure.security.user.TenantNortisUser;

/**
 * テナントのテストユーザ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public class TenantTestUser extends AbstractTestUser {

    private final Tenant tenant;

    private final Authentication authentication;

    @Override
    public NortisUserDetails getUserDetails() {
        return TenantNortisUser.createOfTenant(authentication, tenant, false);
    }

}
