package org.nortis.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.doma.EntityOperation;
import org.nortis.infrastructure.exception.DomainException;

class SuserTest {

    @Test
    void testChangeUsername() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        assertThat(suser.getUsername()).isEqualTo("テストユーザ");

        suser.changeUsername("サンプルユーザ");
        assertThat(suser.getUsername()).isEqualTo("サンプルユーザ");
    }

    @Test
    void testChangePassword() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        assertThat(suser.getHashedPassword()).isEqualTo(HashedPassword.create("password"));

        suser.changePasswordOf(HashedPassword.create("Password$123"));

        assertThat(suser.getHashedPassword()).isEqualTo(HashedPassword.create("Password$123"));
    }

    @Test
    void testLogin() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);

        suser.login();

        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.LOGIN);
    }

    @Test
    void testLogout() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        suser.login();

        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.LOGIN);

        suser.logout();

        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
    }

    @Test
    void testIsJoinTenantOf() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        // 管理者は全てアクセス
        boolean result1 = suser.isJoinTenantOf(TenantId.create("TEST1"));

        assertThat(result1).isTrue();

        Suser suser2 = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.MEMBER,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        boolean result2 = suser2.isJoinTenantOf(TenantId.create("TEST1"));

        assertThat(result2).isTrue();

        boolean result3 = suser2.isJoinTenantOf(TenantId.create("HOGEHOGE"));

        assertThat(result3).isFalse();

    }

    @Test
    void testGetHasRoleOf() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        List<RoleId> roleIds1 = suser.getHasRoleOf(TenantId.create("TEST1"));

        assertThat(roleIds1).hasSize(1);

        List<RoleId> roleIds2 = suser.getHasRoleOf(TenantId.create("HOGEHOGE"));

        assertThat(roleIds2).isEmpty();

    }

    @Test
    void testIsAdmin() throws DomainException {
        Suser suser1 = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        assertThat(suser1.isAdmin()).isTrue();

        Suser suser2 = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.MEMBER,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        assertThat(suser2.isAdmin()).isFalse();
    }

    @Test
    void testGrantTenantAccessOf() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        suser.grantTenantAccessOf(TenantId.create("TEST4"), RoleId.create("00002"));

        assertThat(suser.getUserRoles()).hasSize(2);
        Optional<UserRole> optTenantUser = suser.getUserRoles().stream()
                .filter(data -> data.getTenantId().toString().equals("TEST4")).findFirst();

        assertThat(optTenantUser).isPresent();
        assertThat(optTenantUser.get().getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(optTenantUser.get().getTenantId()).isEqualTo(TenantId.create("TEST4"));
        assertThat(optTenantUser.get().getRoleId()).isEqualTo(RoleId.create("00002"));
        assertThat(optTenantUser.get().getEntityOperation()).isEqualTo(EntityOperation.INSERT);
    }

    @Test
    void testRevokeTenantAccessOf() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        suser.revokeTenantAccessOf(TenantId.create("TEST1"), RoleId.create("00001"));

        assertThat(suser.getUserRoles()).hasSize(1);

        Optional<UserRole> optTenantUser = suser.getUserRoles().stream()
                .filter(data -> data.getTenantId().toString().equals("TEST1")).findFirst();

        assertThat(optTenantUser).isPresent();
        assertThat(optTenantUser.get().getEntityOperation()).isEqualTo(EntityOperation.DELETE);
    }

    @Test
    void testCreate() throws DomainException {
        Suser suser = Suser.create(UserId.create("0000000001"), "テストユーザ", AdminFlg.ADMIN,
                Maps.newHashMap(TenantId.create("TEST1"), RoleId.create("00001")), LoginId.create("TEST"),
                HashedPassword.create("password"));

        assertThat(suser.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(suser.getUsername()).isEqualTo("テストユーザ");
        assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.ADMIN);
        assertThat(suser.getLoginId()).isEqualTo(LoginId.create("TEST"));
        assertThat(suser.getHashedPassword()).isEqualTo(HashedPassword.create("password"));
        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);

        assertThat(suser.getUserRoles()).hasSize(1);
        UserRole tenantUser1 = suser.getUserRoles().get(0);
        assertThat(tenantUser1.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser1.getTenantId()).isEqualTo(TenantId.create("TEST1"));
        assertThat(tenantUser1.getRoleId()).isEqualTo(RoleId.create("00001"));

    }

}
