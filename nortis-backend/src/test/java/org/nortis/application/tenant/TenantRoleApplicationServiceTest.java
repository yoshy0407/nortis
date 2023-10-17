package org.nortis.application.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.application.tenant.model.RoleAuthorityUpdateCommand;
import org.nortis.application.tenant.model.RoleCreateCommand;
import org.nortis.application.tenant.model.RoleNameChangeCommand;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.exception.NoAuthorityDomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.test.user.TestUsers;

class TenantRoleApplicationServiceTest extends ServiceTestBase {

    @Mock
    TenantRepository tenantRepository;

    @Mock
    NumberingDomainService numberingDomainService;

    @Mock
    AuthorityCheckDomainService authorityCheckDomainService;

    TenantRoleApplicationService service;

    @BeforeEach
    void setup() {
        this.service = new TenantRoleApplicationService(tenantRepository, numberingDomainService,
                authorityCheckDomainService);
    }

    @Test
    void testGetOperationAuthorityList() {
        List<OperationAuthority> list = this.service.getOperationAuthorityList(ApplicationTranslator.noConvert());

        assertThat(list).hasSize(OperationAuthority.values().length);
    }

    @Test
    void testGetRole() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId, "テストロール", Lists.list(OperationAuthority.READ_ENDPOINT));

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        TenantRole tenantRole = this.service.getRole(tenantId.toString(), roleId.toString(),
                TestUsers.memberUser().getUserDetails(), ApplicationTranslator.noConvert());

        assertThat(tenantRole).isEqualTo(tenant.getRole(roleId).get());
    }

    @Test
    void testGetRole_TenantNotFound() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        RoleId roleId = RoleId.create("00001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.service.getRole(tenantId.toString(), roleId.toString(), TestUsers.memberUser().getUserDetails(),
                    ApplicationTranslator.nothing());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
    }

    @Test
    void testGetRole_RoleNotFound() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.service.getRole(tenantId.toString(), roleId.toString(), TestUsers.memberUser().getUserDetails(),
                    ApplicationTranslator.nothing());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10005");
    }

    @Test
    void testGetRole_AccessDenied() throws DomainException {

        NortisUserDetails userDetails = TestUsers.memberUser().getUserDetails();
        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(this.authorityCheckDomainService)
                .checkHasReadRole(userDetails, tenant);

        NoAuthorityDomainException ex = assertThrows(NoAuthorityDomainException.class, () -> {
            this.service.getRole(tenantId.toString(), roleId.toString(), userDetails, ApplicationTranslator.nothing());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testGetRoles() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId1 = RoleId.create("00001");
        RoleId roleId2 = RoleId.create("00002");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId1, "テストロール１", Lists.list(OperationAuthority.READ_ENDPOINT));
        tenant.createRole(roleId2, "テストロール２", Lists.list(OperationAuthority.READ_TENANT_ROLE));

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        List<TenantRole> result = this.service.getRoles(tenantId.toString(), TestUsers.memberUser().getUserDetails(),
                ApplicationTranslator.noConvert());

        assertThat(result).hasSize(2);
    }

    @Test
    void testGetRoles_TenantNotFound() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.service.getRoles(tenantId.toString(), TestUsers.memberUser().getUserDetails(),
                    ApplicationTranslator.noConvert());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
    }

    @Test
    void testGetRoles_AccessDenied() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId1 = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId1, "テストロール１", Lists.list(OperationAuthority.READ_ENDPOINT));
        tenant.createRole(roleId1, "テストロール２", Lists.list(OperationAuthority.READ_TENANT_ROLE));

        NortisUserDetails userDetails = TestUsers.memberUser().getUserDetails();

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(this.authorityCheckDomainService)
                .checkHasReadRole(userDetails, tenant);

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.service.getRoles(tenantId.toString(), userDetails, ApplicationTranslator.noConvert());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testCreateRole() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId, "テストロール", Lists.list(OperationAuthority.READ_ENDPOINT));

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        when(this.numberingDomainService.createNewRoleId()).thenReturn(roleId);

        RoleCreateCommand command = new RoleCreateCommand(tenantId.toString(), "テストロール",
                Lists.list(OperationAuthority.READ_ENDPOINT.getAuthorityId()));

        this.service.createRole(command, TestUsers.memberUser().getUserDetails(), ApplicationTranslator.nothing());

        verify(tenantRepository).update(tenant);
    }

    @Test
    void testCreateRole_AccessDenied() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId, "テストロール", Lists.list(OperationAuthority.READ_ENDPOINT));

        NortisUserDetails userDetails = TestUsers.memberUser().getUserDetails();
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(this.authorityCheckDomainService)
                .checkHasWriteRole(userDetails, tenant);

        RoleCreateCommand command = new RoleCreateCommand(tenantId.toString(), "テストロール",
                Lists.list(OperationAuthority.READ_ENDPOINT.getAuthorityId()));

        NoAuthorityDomainException ex = assertThrows(NoAuthorityDomainException.class, () -> {
            this.service.createRole(command, TestUsers.memberUser().getUserDetails(), ApplicationTranslator.nothing());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testChangeRoleName() throws DomainException {

        TenantId tenantId = TenantId.create("0000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId, "テスト", Lists.list(OperationAuthority.READ_ENDPOINT));
        when(tenantRepository.getByTenantId(tenantId)).thenReturn(Optional.of(tenant));

        RoleNameChangeCommand command = new RoleNameChangeCommand(tenantId.toString(), roleId.toString(), "テストロール");
        this.service.changeRoleName(command, TestUsers.memberUser().getUserDetails(), ApplicationTranslator.nothing());

        verify(tenantRepository).update(tenant);
    }

    @Test
    void testUpdateOperationAuthority() throws DomainException {
        TenantId tenantId = TenantId.create("0000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId, "テスト", Lists.list(OperationAuthority.READ_ENDPOINT));
        when(tenantRepository.getByTenantId(tenantId)).thenReturn(Optional.of(tenant));

        RoleAuthorityUpdateCommand command = new RoleAuthorityUpdateCommand(tenantId.toString(), roleId.toString(),
                Lists.list(OperationAuthority.READ_TENANT_ROLE.getAuthorityId()),
                Lists.list(OperationAuthority.READ_ENDPOINT.getAuthorityId()));
        this.service.updateOperationAuthority(command, TestUsers.memberUser().getUserDetails(),
                ApplicationTranslator.nothing());

        verify(tenantRepository).update(tenant);
    }

    @Test
    void testDeleteRole() throws DomainException {
        TenantId tenantId = TenantId.create("0000000001");
        TenantIdentifier tenantIdentifier = TenantIdentifier.create("TEST");
        RoleId roleId = RoleId.create("00001");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テスト");
        tenant.createRole(roleId, "テスト", Lists.list(OperationAuthority.READ_ENDPOINT));
        when(tenantRepository.getByTenantId(tenantId)).thenReturn(Optional.of(tenant));

        this.service.deleteRole(tenantId.toString(), TestUsers.memberUser().getUserDetails(), roleId.toString());

        verify(tenantRepository).update(tenant);
    }

}
