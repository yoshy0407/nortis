package org.nortis.domain.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.Map;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.SuserNortisUser;
import org.nortis.infrastructure.security.user.TenantNortisUser;

class AuthorityCheckDomainServiceTest extends TestBase {

    AuthorityCheckDomainService domainService = new AuthorityCheckDomainService();

    @Test
    void testCheckAdminOf_SuccessWithUser() throws DomainException {
        var userId = UserId.create("0000000001");
        Suser suser = createUser(userId, AdminFlg.ADMIN, TenantId.create("1000000001"));
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkAdminOf(user);
        });
    }

    @Test
    void testCheckAdminOf_FailureWithUser() throws DomainException {
        var userId = UserId.create("0000000001");
        Suser suser = createUser(userId, AdminFlg.MEMBER, TenantId.create("1000000001"));
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkAdminOf(user);
        });
    }

    @Test
    void testCheckAdminOf_FailureWithTenant() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");
        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テストテナント");
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkAdminOf(user);
        });
    }

    @Test
    void testCheckJoinedTenantOf_SuccessWithUser() throws DomainException {
        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("2000000001");
        Suser suser = createUser(userId, AdminFlg.MEMBER, tenantId);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkJoinedTenantOf(user, tenantId);
        });
    }

    @Test
    void testCheckJoinedTenantOf_SuccessWithTenant() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");
        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テストテナント");
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkJoinedTenantOf(user, tenantId);
        });
    }

    @Test
    void testCheckJoinedTenantOf_FailureWithUser() throws DomainException {
        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("2000000001");
        Suser suser = createUser(userId, AdminFlg.MEMBER, tenantId);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkJoinedTenantOf(user, TenantId.create("3000000001"));
        });
    }

    @Test
    void testCheckJoinedTenantOf_FailureWithTenant() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");
        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テストテナント");
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkJoinedTenantOf(user, TenantId.create("2000000001"));
        });
    }

    @Test
    void testCheckHasChangeTenantNameOf_SuccessWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasChangeTenantNameOf(user, tenant);
        });
    }

    @Test
    void testCheckHasChangeTenantNameOf_FailureWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.READ_ENDPOINT);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasChangeTenantNameOf(user, tenant);
        });
    }

    @Test
    void testCheckHasChangeTenantNameOf_SuccessWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasChangeTenantNameOf(user, tenant);
        });
    }

    @Test
    void testCheckHasChangeTenantNameOf_FailureWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication,
                createTenant(TenantId.create("1000000002"), roleId, OperationAuthority.WRITE_TENANT_NAME), false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasChangeTenantNameOf(user, tenant);
        });
    }

    @Test
    void testCheckHasCreateApiKeyOf_SuccessWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_APIKEY);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasCreateApiKeyOf(user, tenant);
        });
    }

    @Test
    void testCheckHasCreateApiKeyOf_FailureWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasCreateApiKeyOf(user, tenant);
        });
    }

    @Test
    void testCheckHasCreateApiKeyOf_SuccessWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasCreateApiKeyOf(user, tenant);
        });
    }

    @Test
    void testCheckHasCreateApiKeyOf_FailureWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication,
                createTenant(TenantId.create("1000000002"), roleId, OperationAuthority.WRITE_TENANT_NAME), false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasCreateApiKeyOf(user, tenant);
        });
    }

    @Test
    void testCheckHasWriteRole_SuccessWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.READWRITE_TENANT_ROLE);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasWriteRole(user, tenant);
        });

    }

    @Test
    void testCheckHasWriteRole_SuccessWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.READWRITE_TENANT_ROLE);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasWriteRole(user, tenant);
        });

    }

    @Test
    void testCheckHasWriteRole_FailureWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.READWRITE_TENANT_ROLE);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication,
                createTenant(TenantId.create("1000000002"), roleId, OperationAuthority.WRITE_TENANT_NAME), false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasWriteRole(user, tenant);
        });

    }

    @Test
    void testCheckHasWriteRole_FailureWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.READ_ENDPOINT);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasWriteRole(user, tenant);
        });
    }

    @Test
    void testCheckHasReadRole_SuccessWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.READ_TENANT_ROLE);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasReadRole(user, tenant);
        });

    }

    @Test
    void testCheckHasReadRole_SuccessWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication, tenant, false);

        assertDoesNotThrow(() -> {
            this.domainService.checkHasReadRole(user, tenant);
        });

    }

    @Test
    void testCheckHasReadRole_FailureWithTenant() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromTenant(tenantId);

        TenantNortisUser user = TenantNortisUser.createOfTenant(authentication,
                createTenant(TenantId.create("1000000002"), roleId, OperationAuthority.WRITE_TENANT_NAME), false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasReadRole(user, tenant);
        });

    }

    @Test
    void testCheckHasReadRole_FailureWithUser() throws DomainException {

        var userId = UserId.create("0000000001");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");

        Suser suser = createUser(userId, AdminFlg.ADMIN, tenantId, roleId);
        Tenant tenant = createTenant(tenantId, roleId, OperationAuthority.WRITE_TENANT_NAME);
        Authentication authentication = Authentication.createFromUserId(userId);

        SuserNortisUser user = SuserNortisUser.createOfUser(suser, authentication, false);

        assertThrows(DomainException.class, () -> {
            this.domainService.checkHasReadRole(user, tenant);
        });
    }

    private Suser createUser(UserId userId, AdminFlg adminFlg, TenantId tenantId) throws DomainException {
        Map<TenantId, RoleId> map = Maps.newHashMap(tenantId, RoleId.create("00001"));
        return Suser.create(userId, "テストユーザ", adminFlg, map, LoginId.create("testUser"),
                HashedPassword.create("password"));

    }

    private Suser createUser(UserId userId, AdminFlg adminFlg, TenantId tenantId, RoleId roleId)
            throws DomainException {
        Map<TenantId, RoleId> map = Maps.newHashMap(tenantId, roleId);
        return Suser.create(userId, "テストユーザ", adminFlg, map, LoginId.create("testUser"),
                HashedPassword.create("password"));

    }

    private Tenant createTenant(TenantId tenantId, RoleId roleId, OperationAuthority authority) throws DomainException {
        var tenantIdentifier = TenantIdentifier.create("TEST");
        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テストテナント");

        TenantRole role = TenantRole.create(tenantId, roleId, "テストロール");
        role.grantAuthority(authority);
        tenant.getRoles().put(role.getRoleId(), role);

        return tenant;
    }

}
