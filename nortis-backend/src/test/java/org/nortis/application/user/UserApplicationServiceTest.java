package org.nortis.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.application.user.model.SuserChangeNameCommand;
import org.nortis.application.user.model.SuserGrantRoleCommand;
import org.nortis.application.user.model.SuserRevokeRoleCommand;
import org.nortis.application.user.model.TenantRoleModel;
import org.nortis.application.user.model.UserRegisterCommand;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.service.PasswordDomainService;
import org.nortis.domain.service.SuserDomainService;
import org.nortis.domain.service.TenantDomainService;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.user.TestUsers;

class UserApplicationServiceTest extends ServiceTestBase {

    @Mock
    AuthorityCheckDomainService authorityCheckDomainService;

    @Mock
    SuserRepository suserRepository;

    @Mock
    SuserDomainService suserDomainService;

    @Mock
    PasswordDomainService passwordDomainService;

    @Mock
    NumberingDomainService numberingDomainService;

    @Mock
    TenantDomainService tenantDomainService;

    UserApplicationService suserApplicationService;

    @BeforeEach
    void setup() {
        this.suserApplicationService = new UserApplicationService(authorityCheckDomainService, suserRepository,
                suserDomainService, passwordDomainService, numberingDomainService, tenantDomainService);
    }

    @Test
    void testRegisterUser() throws DomainException {

        doReturn(UserId.create("0000000001")).when(numberingDomainService).createNewUserId();
        doReturn(HashedPassword.create("password")).when(passwordDomainService).hashPassword(eq("password"));

        UserRegisterCommand command = new UserRegisterCommand("テスト", "test", "password", 0,
                Lists.list(TenantRoleModel.builder().roleId("00001").tenantId("1000000001").build()));

        Suser suser = this.suserApplicationService.registerUser(command, TestUsers.adminUser().getUserDetails(),
                ApplicationTranslator.noConvert());

        verify(suserRepository).save(suser);
    }

    @Test
    void testChangeName() throws DomainException {

        UserId userId = UserId.create("0000000001");

        Suser suser = Suser.create(userId, "テストテスト", AdminFlg.ADMIN, Collections.emptyMap(), LoginId.create("TEST"),
                HashedPassword.create("password"));
        when(this.suserRepository.getByUserId(userId)).thenReturn(Optional.of(suser));

        SuserChangeNameCommand command = new SuserChangeNameCommand(userId.toString(), "テスト");
        this.suserApplicationService.changeName(command, ApplicationTranslator.noConvert());

        verify(suserRepository).update(suser);
        assertThat(suser.getUsername()).isEqualTo("テスト");
    }

    @Test
    void testChangePassword() throws DomainException {

        UserId userId = UserId.create("0000000001");

        Suser suser = Suser.create(userId, "テストテスト", AdminFlg.ADMIN, Collections.emptyMap(), LoginId.create("TEST"),
                HashedPassword.create("password"));
        when(this.suserRepository.getByUserId(eq(userId))).thenReturn(Optional.of(suser));
        when(this.passwordDomainService.hashPassword(eq("password123")))
                .thenReturn(HashedPassword.create("password123"));

        this.suserApplicationService.changePassword(userId.toString(), "password123");

        verify(suserRepository).update(suser);
    }

    @Test
    void testResetPasswordOf() throws DomainException {

        UserId userId = UserId.create("0000000001");

        Suser suser = Suser.create(userId, "テストテスト", AdminFlg.ADMIN, Collections.emptyMap(), LoginId.create("TEST"),
                HashedPassword.create("password"));
        when(this.suserRepository.getByUserId(userId)).thenReturn(Optional.of(suser));
        when(this.passwordDomainService.createResetPassword()).thenReturn("password123");
        when(this.passwordDomainService.hashPassword(eq("password123")))
                .thenReturn(HashedPassword.create("password123"));

        this.suserApplicationService.resetPassword(userId.toString());

        verify(suserRepository).update(suser);
    }

    @Test
    void testGrantRole() throws DomainException {
        UserId userId = UserId.create("0000000001");

        Suser suser = Suser.create(userId, "テストテスト", AdminFlg.ADMIN, Collections.emptyMap(), LoginId.create("TEST"),
                HashedPassword.create("password"));
        when(this.suserRepository.getByUserId(userId)).thenReturn(Optional.of(suser));

        SuserGrantRoleCommand command = SuserGrantRoleCommand.builder().userId(userId.toString())
                .tenantRoleList(Lists.list(new TenantRoleModel("1000000001", "00001"))).build();
        this.suserApplicationService.grantRole(command, ApplicationTranslator.noConvert());

        verify(suserRepository).update(suser);
    }

    @Test
    void testRevokeRole() throws DomainException {
        UserId userId = UserId.create("0000000001");

        Suser suser = Suser.create(userId, "テストテスト", AdminFlg.ADMIN, Collections.emptyMap(), LoginId.create("TEST"),
                HashedPassword.create("password"));
        when(this.suserRepository.getByUserId(userId)).thenReturn(Optional.of(suser));

        SuserRevokeRoleCommand command = SuserRevokeRoleCommand.builder().userId(userId.toString())
                .tenantRoleList(Lists.list(new TenantRoleModel("1000000001", "00001"))).build();
        this.suserApplicationService.revokeRole(command, ApplicationTranslator.noConvert());

        verify(suserRepository).update(suser);
    }

    @Test
    void testDeleteUserOf() throws DomainException {
        UserId userId = UserId.create("0000000001");

        Suser suser = Suser.create(userId, "テストテスト", AdminFlg.ADMIN, Collections.emptyMap(), LoginId.create("TEST"),
                HashedPassword.create("password"));
        when(this.suserRepository.getByUserId(userId)).thenReturn(Optional.of(suser));

        this.suserApplicationService.deleteUserOf(userId.toString());

        verify(suserRepository).remove(suser);
    }

}
