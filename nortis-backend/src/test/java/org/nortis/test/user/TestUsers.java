package org.nortis.test.user;

import java.util.Collections;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;

/**
 * テストユーザに関するクラス
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class TestUsers {

    /**
     * 管理者ユーザを作成します
     * 
     * @return 管理者のテストユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public static SuserTestUser adminUser() throws DomainException {
        Suser suser = Suser.create(UserId.create("1000000001"), "テスト 管理者", AdminFlg.ADMIN, Collections.emptyMap(),
                LoginId.create("TEST"), HashedPassword.create("password"));
        Authentication authentication = Authentication.createFromUserId(suser.getUserId());
        return new SuserTestUser(suser, authentication);
    }

    /**
     * メンバーユーザを作成します
     * 
     * @return メンバーのテストユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public static SuserTestUser memberUser() throws DomainException {
        Suser suser = Suser.create(UserId.create("1000000001"), "テスト メンバー", AdminFlg.MEMBER, Collections.emptyMap(),
                LoginId.create("TEST"), HashedPassword.create("password"));
        Authentication authentication = Authentication.createFromUserId(suser.getUserId());
        return new SuserTestUser(suser, authentication);
    }

    /**
     * テナントユーザを作成します
     * 
     * @return テナントユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public static TenantTestUser tenantUser() throws DomainException {
        Tenant tenant = Tenant.create(TenantId.create("2000000001"), TenantIdentifier.create("TEST"), "テストテナント");
        Authentication authentication = Authentication.createFromTenant(tenant.getTenantId());
        return new TenantTestUser(tenant, authentication);
    }
}
