package org.nortis.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nortis.TestBase;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;

@ExtendWith(MockitoExtension.class)
class SuserDomainServiceTest extends TestBase {

    @Mock
    SuserRepository suserRepository;

    SuserDomainService domainService;

    @BeforeEach
    void setup() {
        this.domainService = new SuserDomainService(this.suserRepository);
    }

    @Test
    void testBeforeRegisterCheck_Success() throws DomainException {
        var userId = UserId.create("0000000001");
        var loginId = LoginId.create("TEST");

        when(this.suserRepository.getByUserId(eq(userId))).thenReturn(Optional.empty());
        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            this.domainService.beforeRegisterCheck(userId, loginId);
        });
    }

    @Test
    void testBeforeRegisterCheck_UserIdExists() throws DomainException {
        var userId = UserId.create("0000000001");
        var loginId = LoginId.create("TEST");

        when(this.suserRepository.getByUserId(eq(userId))).thenReturn(Optional.of(Suser.create(userId, "テスト",
                AdminFlg.ADMIN, Collections.emptyMap(), loginId, HashedPassword.create("password"))));

        assertThrows(UnexpectedException.class, () -> {
            this.domainService.beforeRegisterCheck(userId, loginId);
        });
    }

    @Test
    void testBeforeRegisterCheck_LoginIdExists() throws DomainException {
        var userId = UserId.create("0000000001");
        var loginId = LoginId.create("TEST");

        when(this.suserRepository.getByUserId(eq(userId))).thenReturn(Optional.empty());
        when(this.suserRepository.getByLoginId(eq(loginId))).thenReturn(Optional.of(Suser.create(userId, "テスト",
                AdminFlg.ADMIN, Collections.emptyMap(), loginId, HashedPassword.create("password"))));

        assertThrows(DomainException.class, () -> {
            this.domainService.beforeRegisterCheck(userId, loginId);
        });
    }

}
