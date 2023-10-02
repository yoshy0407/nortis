package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nortis.TestBase;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;

@ExtendWith(MockitoExtension.class)
class TenantDomainServiceTest extends TestBase {

    @Mock
    TenantRepository tenantRepository;

    @Mock
    NumberingDomainService numberingDomainService;

    @InjectMocks
    TenantDomainService domainService;

    @Test
    void testBeforeRegisterCheck_success() throws DomainException {

        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());
        when(tenantRepository.getByTenantIdentifier(eq(tenantIdentifier))).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            domainService.beforeRegisterCheck(tenantId, tenantIdentifier);
        });
    }

    @Test
    void testBeforeRegisterCheck_failExistTenantId() throws DomainException {

        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        when(tenantRepository.getByTenantId(eq(tenantId)))
                .thenReturn(Optional.of(Tenant.create(tenantId, tenantIdentifier, "テスト")));

        UnexpectedException ex = assertThrows(UnexpectedException.class, () -> {
            domainService.beforeRegisterCheck(tenantId, tenantIdentifier);
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10004");
    }

    @Test
    void testBeforeRegisterCheck_failExistTenantIdentifier() throws DomainException {

        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        when(tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());
        when(tenantRepository.getByTenantIdentifier(eq(tenantIdentifier)))
                .thenReturn(Optional.of(Tenant.create(tenantId, tenantIdentifier, "テスト")));

        DomainException ex = assertThrows(DomainException.class, () -> {
            domainService.beforeRegisterCheck(tenantId, tenantIdentifier);
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10001");
    }

    @Test
    void testCreateTenant() throws DomainException {

        var tenantId = TenantId.create("100000001");
        var tenantIdentifer = TenantIdentifier.create("TEST");
        var roleId1 = RoleId.create("00001");
        var roleId2 = RoleId.create("00002");

        when(numberingDomainService.createNewRoleId()).thenReturn(roleId1, roleId2);

        Tenant tenant = domainService.createTenant(tenantId, tenantIdentifer, "テスト");

        assertThat(tenant.getRoles()).hasSize(2);
        assertThat(tenant.getRole(roleId1)).isPresent();
        assertThat(tenant.getRole(roleId2)).isPresent();

    }

    @Test
    void testCheckExistRole_Exist() throws DomainException {

        var tenantId = TenantId.create("100000001");
        var roleId = RoleId.create("00001");

        when(this.tenantRepository.getByTenantId(eq(tenantId)))
                .thenReturn(Optional.of(createTenant(tenantId, roleId, OperationAuthority.READ_ENDPOINT)));

        assertDoesNotThrow(() -> {
            this.domainService.checkExistRole(tenantId, roleId);
        });
    }

    @Test
    void testCheckExistRole_NotExist() throws DomainException {

        var tenantId = TenantId.create("100000001");
        var roleId = RoleId.create("00001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(
                Optional.of(createTenant(tenantId, RoleId.create("00002"), OperationAuthority.READ_ENDPOINT)));

        assertThrows(DomainException.class, () -> {
            this.domainService.checkExistRole(tenantId, roleId);
        });
    }

    @Test
    void testCheckExistTenantOf_Success() throws DomainException {

        var tenantId = TenantId.create("1000000001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(
                Optional.of(createTenant(tenantId, RoleId.create("00002"), OperationAuthority.READ_ENDPOINT)));

        assertDoesNotThrow(() -> {
            this.domainService.checkExistTenantOf(tenantId);
        });
    }

    @Test
    void testCheckExistTenantOf_Failure() throws DomainException {

        var tenantId = TenantId.create("1000000001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> {
            this.domainService.checkExistTenantOf(tenantId);
        });
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
