package org.nortis.domain.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;

class TenantTest extends TestBase {

    @Test
    void testChangeTenantName() throws DomainException {
        Tenant tenant = Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST1"), "TEST1 Tenant");

        tenant.changeTenantName("TEST11 Tenant");

        assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(tenant.getTenantName()).isEqualTo("TEST11 Tenant");
    }

    @Test
    void testCreateEndpoint() throws DomainException {
        Tenant tenant = Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST1"), "TEST1 Tenant");

        Endpoint endpoint = tenant.createEndpoint(EndpointId.create("2000000001"), EndpointIdentifier.create("TEST"),
                "TEST_ENDPOINT");

        assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("2000000001"));
        assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(endpoint.getEndpointIdentifier()).isEqualTo(EndpointIdentifier.create("TEST"));
        assertThat(endpoint.getEndpointName()).isEqualTo("TEST_ENDPOINT");
    }

    @Test
    void testCreate() throws DomainException {
        Tenant tenant = Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST"), "TEST TENANT");

        assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(tenant.getTenantIdentifier()).isEqualTo(TenantIdentifier.create("TEST"));
        assertThat(tenant.getTenantName()).isEqualTo("TEST TENANT");
    }

    @Test
    void testCreateRole() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var roleId = RoleId.create("00001");
        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "TEST TENANT");

        tenant.createRole(roleId, "TEST ROLE", Lists.list(OperationAuthority.WRITE_APIKEY));

        Suser suser = creatSuser(tenantId, roleId);
        boolean result = tenant.permitOperation(suser, OperationId.WRITE_APIKEY);
        assertThat(result).isTrue();
    }

    @Test
    void testGrantRoleAuthority() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var roleId = RoleId.create("00001");
        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "TEST TENANT");

        tenant.createRole(roleId, "TEST ROLE", Lists.list(OperationAuthority.WRITE_APIKEY));
        tenant.grantRoleAuthority(roleId, OperationAuthority.READ_ENDPOINT);

        Suser suser = creatSuser(tenantId, roleId);
        boolean result = tenant.permitOperation(suser, OperationId.READ_ENDPOINT);
        assertThat(result).isTrue();
    }

    @Test
    void testRevokeRoleAuthority() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var roleId = RoleId.create("00001");
        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "TEST TENANT");

        tenant.createRole(roleId, "TEST ROLE", Lists.list(OperationAuthority.WRITE_APIKEY));
        tenant.revokeRoleAuthority(roleId, OperationAuthority.WRITE_APIKEY);

        Suser suser = creatSuser(tenantId, roleId);
        boolean result = tenant.permitOperation(suser, OperationId.WRITE_APIKEY);
        assertThat(result).isFalse();
    }

    @Test
    void testDeleteRole() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var roleId = RoleId.create("00001");
        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "TEST TENANT");

        tenant.createRole(roleId, "TEST ROLE", Lists.list(OperationAuthority.WRITE_APIKEY));
        tenant.deleteRole(roleId);

        Suser suser = creatSuser(tenantId, roleId);
        boolean result = tenant.permitOperation(suser, OperationId.WRITE_APIKEY);
        assertThat(result).isFalse();
    }

    @Test
    void testCreateApiKey() throws DomainException {
        Tenant tenant = Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST"), "TEST TENANT");
        Authentication auth = tenant.createApiKey();

        assertThat(auth.getApiKey()).isNotNull();
        assertThat(auth.getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(auth.getUserId()).isNull();
    }

    @Test
    void testCreateTenantIdNull() {
        assertThrows(DomainException.class, () -> {
            Tenant.create(null, TenantIdentifier.create("TEST"), "");

        }, "テナントIDが未設定です");
    }

    @Test
    void testCreateTenantNameEmpty() {
        assertThrows(DomainException.class, () -> {
            Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST"), "");
        }, "テナント名が未設定です");
    }

    @Test
    void testCreateTenantNameNull() {
        assertThrows(DomainException.class, () -> {
            Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST"), null);
        }, "テナント名が未設定です");
    }

    @Test
    void testCreateTenantNameLength() {
        assertThrows(DomainException.class, () -> {
            Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST"),
                    "123456789012345678901234567890123456789012345678901");

        }, "テナント名は50文字以内である必要があります");
    }

    private Suser creatSuser(TenantId tenantId, RoleId roleId) throws DomainException {
        return Suser.create(UserId.create("1000000001"), "TEST USER", AdminFlg.MEMBER,
                Maps.newHashMap(tenantId, roleId), LoginId.create("TEST"), HashedPassword.create("password"));
    }
}
