package org.nortis.domain.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.doma.EntityOperation;
import org.nortis.infrastructure.exception.DomainException;

class TenantRoleTest {

    @Test
    void testCanOperationOf_True() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");
        TenantRole role = TenantRole.create(tenantId, roleId, "TEST ROLE");
        // :TOOD READWRITEの判定難しい
        role.grantAuthority(OperationAuthority.READWRITE_ENDPOINT);

        boolean result = role.canOperationOf(OperationId.READ_ENDPOINT);
        assertThat(result).isTrue();
    }

    @Test
    void testCanOperationOf_False() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");
        TenantRole role = TenantRole.create(tenantId, roleId, "TEST ROLE");
        // :TOOD READWRITEの判定難しい
        role.grantAuthority(OperationAuthority.READWRITE_ENDPOINT);

        boolean result = role.canOperationOf(OperationId.WRITE_APIKEY);
        assertThat(result).isFalse();
    }

    @Test
    void testRevokeAuthorityAndCheck() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");
        TenantRole role = TenantRole.create(tenantId, roleId, "TEST ROLE");
        // :TOOD READWRITEの判定難しい
        role.grantAuthority(OperationAuthority.READWRITE_ENDPOINT);

        boolean result = role.canOperationOf(OperationId.READ_ENDPOINT);
        assertThat(result).isTrue();

        role.revokeAuthority(OperationAuthority.READWRITE_ENDPOINT);

        boolean result2 = role.canOperationOf(OperationId.READ_ENDPOINT);
        assertThat(result2).isFalse();

    }

    @Test
    void testDelete() throws DomainException {

        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");
        TenantRole role = TenantRole.create(tenantId, roleId, "TEST ROLE");

        role.grantAuthority(OperationAuthority.READ_ENDPOINT);

        role.delete();

        assertThat(role.getEntityOperation()).isEqualTo(EntityOperation.DELETE);
        assertThat(role.getRoleOperations().get(OperationId.READ_ENDPOINT).getEntityOperation())
                .isEqualTo(EntityOperation.DELETE);
    }

}
