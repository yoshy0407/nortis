package org.nortis.test.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.user.Suser;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.infrastructure.security.user.SuserNortisUser;

/**
 * ユーザのテストユーザ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public class SuserTestUser implements TestUser {

    private final Suser suser;

    private final Authentication authentication;

    @Override
    public NortisUserDetails getUserDetails() {
        return SuserNortisUser.createOfUser(suser, authentication, false);
    }

}
