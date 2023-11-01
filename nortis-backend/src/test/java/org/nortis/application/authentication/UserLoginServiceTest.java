package org.nortis.application.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.service.PasswordDomainService;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

class UserLoginServiceTest extends ServiceTestBase {

    @Mock
    SuserRepository suserRepository;

    @Mock
    AuthenticationRepository authenticationRepository;

    @Mock
    PasswordDomainService passwordDomainService;

    UserLoginService service;

    @BeforeEach
    void setup() {
        this.service = new UserLoginService(suserRepository, authenticationRepository, passwordDomainService);
    }

    @Test
    void testLogin_success() throws DomainException {

        var loginId = LoginId.create("test");
        var password = HashedPassword.create("password");
        var userId = UserId.create("000000001");

        Suser suser = Suser.create(userId, "テストユーザ", AdminFlg.MEMBER, Collections.emptyMap(), loginId, password);
        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.of(suser));

        // anyじゃないとなぜかモックが動作しない
        doReturn(true).when(this.passwordDomainService).match(anyString(), any());

        Authentication authentication = service.login(loginId.toString(), password.toString(),
                ApplicationTranslator.noConvert());

        verify(suserRepository).update(eq(suser));
        verify(authenticationRepository).save(authentication);

        assertThat(authentication.getApiKey()).isNotNull();
        assertThat(authentication.getTenantId()).isNull();
        assertThat(authentication.getUserId()).isEqualTo(userId);
        assertThat(authentication.getLastAccessDatetime()).isNotNull();
    }

    @Test
    void testLogin_loginIdNotFound() throws DomainException {

        var loginId = LoginId.create("test");
        var password = HashedPassword.create("password");

        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.empty());

        // anyじゃないとなぜかモックが動作しない
        doReturn(true).when(this.passwordDomainService).match(anyString(), any());

        assertThrows(DomainException.class, () -> {
            service.login(loginId.toString(), password.toString(), ApplicationTranslator.noConvert());
        });

    }

    @Test
    void testLogin_passwordNotMatch() throws DomainException {

        var loginId = LoginId.create("test");
        var password = HashedPassword.create("password");
        var userId = UserId.create("000000001");

        Suser suser = Suser.create(userId, "テストユーザ", AdminFlg.MEMBER, Collections.emptyMap(), loginId, password);
        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.of(suser));

        // anyじゃないとなぜかモックが動作しない
        doReturn(false).when(this.passwordDomainService).match(anyString(), any());

        assertThrows(DomainException.class, () -> {
            service.login(loginId.toString(), "Password$123", ApplicationTranslator.noConvert());
        });
    }

    @Test
    void testLogout_authenticatedUser() throws DomainException {

        var loginId = LoginId.create("test");
        var password = HashedPassword.create("password");
        var userId = UserId.create("000000001");

        Suser suser = Suser.create(userId, "テストユーザ", AdminFlg.MEMBER, Collections.emptyMap(), loginId, password);
        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.of(suser));

        Authentication authentication = Authentication.createFromUserId(userId);
        when(this.authenticationRepository.getFromUserId(eq(userId))).thenReturn(Optional.ofNullable(authentication));

        assertDoesNotThrow(() -> {
            this.service.logout("test");
        });

        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);

        verify(this.suserRepository).update(eq(suser));
        verify(this.authenticationRepository).remove(eq(authentication));
    }

    @Test
    void testLogout_notAuthenticatedUser() throws DomainException {

        var loginId = LoginId.create("test");
        var password = HashedPassword.create("password");
        var userId = UserId.create("000000001");

        Suser suser = Suser.create(userId, "テストユーザ", AdminFlg.MEMBER, Collections.emptyMap(), loginId, password);
        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.of(suser));

        when(this.authenticationRepository.getFromUserId(eq(userId))).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            this.service.logout("test");
        });

        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);

        verify(this.suserRepository).update(eq(suser));
        verify(this.authenticationRepository, never()).remove(any());
    }

}
