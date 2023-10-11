package org.nortis.application.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.application.tenant.model.TenantNameUpdateCommand;
import org.nortis.application.tenant.model.TenantRegisterCommand;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.service.TenantDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DataNotFoundException;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.exception.NoAuthorityDomainException;
import org.nortis.test.user.TestUsers;

class TenantApplicationServiceTest extends ServiceTestBase {

    @Mock
    TenantRepository tenantRepository;

    @Mock
    AuthenticationRepository authenticationRepository;

    @Mock
    NumberingDomainService numberingDomainService;

    @Mock
    AuthorityCheckDomainService authorityCheckDomainService;

    @Mock
    TenantDomainService tenantDomainService;

    TenantApplicationService tenantApplicationService;

    @BeforeEach
    void setup() {
        this.tenantApplicationService = new TenantApplicationService(tenantRepository, authenticationRepository,
                tenantDomainService, numberingDomainService, authorityCheckDomainService);
    }

    @Test
    void testGet_success() throws DomainException {
        TenantId tenantId = TenantId.create("0000000001");

        Tenant tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST1"), "テストテナント1");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.ofNullable(tenant));

        Tenant result = this.tenantApplicationService.getTenant("0000000001", TestUsers.memberUser().getUserDetails(),
                ApplicationTranslator.noConvert());

        assertThat(result.getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(result.getTenantIdentifier()).isEqualTo(TenantIdentifier.create("TEST1"));
        assertThat(result.getTenantName()).isEqualTo("テストテナント1");
    }

    @Test
    void testGetTenant_NotFound() throws DomainException {
        assertThrows(DataNotFoundException.class, () -> {
            this.tenantApplicationService.getTenant("0000000100", TestUsers.adminUser().getUserDetails(),
                    ApplicationTranslator.nothing());
        });
    }

    @Test
    void testGetListTenant() throws DomainException {
        Paging paging = new Paging(1, 5);

        Tenant tenant1 = Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST1"), "テストテナント1");
        Tenant tenant2 = Tenant.create(TenantId.create("0000000002"), TenantIdentifier.create("TEST2"), "テストテナント2");
        Tenant tenant3 = Tenant.create(TenantId.create("0000000003"), TenantIdentifier.create("TEST3"), "テストテナント3");
        Tenant tenant4 = Tenant.create(TenantId.create("0000000004"), TenantIdentifier.create("TEST4"), "テストテナント4");
        Tenant tenant5 = Tenant.create(TenantId.create("0000000005"), TenantIdentifier.create("TEST5"), "テストテナント5");
        when(this.tenantRepository.getTenantPaging(eq(paging)))
                .thenReturn(Lists.list(tenant1, tenant2, tenant3, tenant4, tenant5));

        List<Tenant> list = this.tenantApplicationService.getListTenant(paging, TestUsers.memberUser().getUserDetails(),
                ApplicationTranslator.noConvert());

        assertThat(list).hasSize(5);
        assertThat(list.get(0).getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(list.get(1).getTenantId()).isEqualTo(TenantId.create("0000000002"));
        assertThat(list.get(2).getTenantId()).isEqualTo(TenantId.create("0000000003"));
        assertThat(list.get(3).getTenantId()).isEqualTo(TenantId.create("0000000004"));
        assertThat(list.get(4).getTenantId()).isEqualTo(TenantId.create("0000000005"));
    }

    @Test
    void testRegister_Success() throws DomainException {

        var testUser = TestUsers.adminUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifer = TenantIdentifier.create("TENANT1");
        var tenantName = "登録テナント";

        when(numberingDomainService.createNewTenantId()).thenReturn(TenantId.create("0000000001"));

        Tenant tenant = Tenant.create(tenantId, tenantIdentifer, tenantName);
        when(tenantDomainService.createTenant(eq(tenantId), eq(tenantIdentifer), eq(tenantName))).thenReturn(tenant);

        TenantRegisterCommand command = new TenantRegisterCommand(tenantIdentifer.toString(), tenantName);
        tenantApplicationService.register(command, testUser.getUserDetails(), ApplicationTranslator.nothing());

        verify(tenantRepository).save(any());
    }

    @Test
    void testRegister_CheckError() throws DomainException {
        var testUser = TestUsers.memberUser();

        TenantRegisterCommand command = new TenantRegisterCommand("TENANT1", "登録テナント");

        doThrow(new DomainException(MessageCodes.nortis10001())).when(tenantDomainService).beforeRegisterCheck(any(),
                any());

        DomainException ex = assertThrows(DomainException.class, () -> {
            tenantApplicationService.register(command, testUser.getUserDetails(), ApplicationTranslator.nothing());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10001");
    }

    @Test
    void testRegister_AccessDenied() throws DomainException {
        var testUser = TestUsers.memberUser();

        TenantRegisterCommand command = new TenantRegisterCommand("TENANT1", "登録テナント");

        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(authorityCheckDomainService)
                .checkAdminOf(eq(testUser.getUserDetails()));

        NoAuthorityDomainException ex = assertThrows(NoAuthorityDomainException.class, () -> {
            tenantApplicationService.register(command, testUser.getUserDetails(), ApplicationTranslator.nothing());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testCreateApiKey_Success() throws DomainException {

        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(TenantId.create("0000000001")))).thenReturn(Optional.of(tenant));
        when(this.authenticationRepository.getFromTenantId(eq(tenantId))).thenReturn(Optional.empty());

        ApiKey apiKey = tenantApplicationService.createApiKey(tenantId.toString(), testUser.getUserDetails());

        assertThat(apiKey).isNotNull();
        verify(authenticationRepository, never()).remove(any());
        verify(authenticationRepository).save(any());
    }

    @Test
    void testCreateApiKey_ReplaceAndSuccess() throws DomainException {

        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(TenantId.create("0000000001")))).thenReturn(Optional.of(tenant));
        when(this.authenticationRepository.getFromTenantId(eq(tenantId)))
                .thenReturn(Optional.of(Authentication.createFromTenant(tenantId)));

        ApiKey apiKey = tenantApplicationService.createApiKey(tenantId.toString(), testUser.getUserDetails());

        assertThat(apiKey).isNotNull();
        verify(authenticationRepository).remove(any());
        verify(authenticationRepository).save(any());
    }

    @Test
    void testCreateApiKey_AccessDenied() throws DomainException {

        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(TenantId.create("0000000001")))).thenReturn(Optional.of(tenant));
        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(this.authorityCheckDomainService)
                .checkHasCreateApiKeyOf(eq(testUser.getUserDetails()), eq(tenant));

        NoAuthorityDomainException ex = assertThrows(NoAuthorityDomainException.class, () -> {
            tenantApplicationService.createApiKey(tenantId.toString(), testUser.getUserDetails());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testCreateApiKey_NotFound() throws DomainException {
        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> {
            tenantApplicationService.createApiKey("AAAAAA", testUser.getUserDetails());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
    }

    @Test
    void testChangeName_Success() throws DomainException {

        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(TenantId.create("0000000001")))).thenReturn(Optional.of(tenant));

        TenantNameUpdateCommand command = new TenantNameUpdateCommand(tenantId.toString(), "テストテナント");
        tenantApplicationService.changeName(command, testUser.getUserDetails(), ApplicationTranslator.nothing());

        verify(this.tenantRepository).update(any());
    }

    @Test
    void testChangeName_AccessDenied() throws DomainException {

        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(this.authorityCheckDomainService)
                .checkHasChangeTenantNameOf(eq(testUser.getUserDetails()), eq(tenant));

        TenantNameUpdateCommand command = new TenantNameUpdateCommand(tenantId.toString(), "テストテナント");

        DomainException ex = assertThrows(DomainException.class, () -> {
            tenantApplicationService.changeName(command, testUser.getUserDetails(), data -> data.getTenantId());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testChangeName_NotFound() throws DomainException {

        var testUser = TestUsers.memberUser();
        var tenantId = TenantId.create("0000000001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> {
            TenantNameUpdateCommand command = new TenantNameUpdateCommand(tenantId.toString(), "テストテナント");
            tenantApplicationService.changeName(command, testUser.getUserDetails(), data -> data.getTenantId());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
    }

    @Test
    void testDelete_Success() throws DomainException {

        var testUser = TestUsers.adminUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));

        tenantApplicationService.delete(tenantId.toString(), testUser.getUserDetails());

        verify(tenantRepository).remove(eq(tenant));
    }

    @Test
    void testDelete_AccessDenied() throws DomainException {

        var testUser = TestUsers.adminUser();
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テナント");
        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.of(tenant));
        doThrow(new NoAuthorityDomainException(MessageCodes.nortis50005())).when(authorityCheckDomainService)
                .checkAdminOf(eq(testUser.getUserDetails()));

        NoAuthorityDomainException ex = assertThrows(NoAuthorityDomainException.class, () -> {
            tenantApplicationService.delete(tenantId.toString(), testUser.getUserDetails());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
    }

    @Test
    void testDelete_NotFound() throws DomainException {
        var testUser = TestUsers.adminUser();
        var tenantId = TenantId.create("0000000001");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> {
            tenantApplicationService.delete(tenantId.toString(), testUser.getUserDetails());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
    }

}
