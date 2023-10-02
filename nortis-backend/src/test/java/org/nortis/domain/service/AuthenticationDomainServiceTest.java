package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nortis.TestBase;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.SuserNortisUser;
import org.nortis.infrastructure.security.user.TenantNortisUser;

@ExtendWith(MockitoExtension.class)
class AuthenticationDomainServiceTest extends TestBase {

    @Mock
    AuthenticationRepository authenticationRepository;

    @Mock
    TenantRepository tenantRepository;

    @Mock
    SuserRepository suserRepository;

    AuthenticationDomainService domainService;

    @BeforeEach
    void setup() {
        this.domainService = new AuthenticationDomainService(authenticationRepository, tenantRepository,
                suserRepository, 600);
    }

    @Test
    void testCheckExpired_Tenant() throws DomainException {
        Authentication auth = Authentication.createFromTenant(TenantId.create("TENANT"));
        auth.setLastAccessDatetime(LocalDateTime.now());
        assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isFalse();
    }

    @Test
    void testCheckExpired_UserNotExpire() throws DomainException {
        Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
        auth.setLastAccessDatetime(LocalDateTime.now());
        assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isFalse();
    }

    @Test
    void testCheckExpired_UserExpire() throws DomainException {
        Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
        auth.setLastAccessDatetime(LocalDateTime.now().minusSeconds(1200));
        assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isTrue();
    }

    @Test
    void testCheckExpired_Null() throws DomainException {
        Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
        assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isFalse();
    }

    @Test
    void testAuthorize_AuthorizeIsSuccessWithUser() throws DomainException {
        UserId userId = UserId.create("0000000001");
        LoginId loginId = LoginId.create("testUser");
        Authentication auth = Authentication.createFromUserId(userId);

        when(this.authenticationRepository.get(eq(auth.getApiKey()))).thenReturn(Optional.ofNullable(auth));

        Suser suser = Suser.create(userId, "テストユーザ", AdminFlg.MEMBER, Collections.emptyMap(), loginId,
                HashedPassword.create("password"));

        when(this.suserRepository.getByUserId(eq(userId))).thenReturn(Optional.of(suser));

        assertDoesNotThrow(() -> {
            var userDetails = this.domainService.authorize(auth.getApiKey());
            assertThat(userDetails).isInstanceOf(SuserNortisUser.class);
            assertThat(userDetails.isUser()).isTrue();
        });

        assertThat(auth.getLastAccessDatetime()).isNotNull();
    }

    @Test
    void testAuthorize_AuthorizeIsFailureWithUser() throws DomainException {
        ApiKey apiKey = ApiKey.newKey();
        when(this.authenticationRepository.get(eq(apiKey))).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> {
            this.domainService.authorize(apiKey);
        });
    }

    @Test
    void testAuthorize_AuthorizeIsSuccessWithTenant() throws DomainException {

        TenantId tenantId = TenantId.create("1000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST");
        Authentication auth = Authentication.createFromTenant(tenantId);

        when(this.authenticationRepository.get(eq(auth.getApiKey()))).thenReturn(Optional.ofNullable(auth));

        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テストテナント");

        when(this.tenantRepository.getByTenantId(eq(tenantId))).thenReturn(Optional.ofNullable(tenant));

        assertDoesNotThrow(() -> {
            var userDetails = this.domainService.authorize(auth.getApiKey());
            assertThat(userDetails).isInstanceOf(TenantNortisUser.class);
            assertThat(userDetails.isTenant()).isTrue();
        });
        assertThat(auth.getLastAccessDatetime()).isNotNull();
    }

    @Test
    void testAuthorize_AuthorizeIsExpiredFailureWithUser() throws DomainException {
        Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
        auth.setLastAccessDatetime(LocalDateTime.now().minusSeconds(1200));

        when(this.authenticationRepository.get(eq(auth.getApiKey()))).thenReturn(Optional.ofNullable(auth));

        assertThrows(DomainException.class, () -> {
            this.domainService.authorize(auth.getApiKey());
        });
    }

}
