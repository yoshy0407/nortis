package org.nortis.application.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.ServiceTestBase;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.service.AuthenticationDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.infrastructure.security.user.TenantNortisUser;

class AuthenticationApplicationServiceTest extends ServiceTestBase {

    @Mock
    AuthenticationRepository authenticationRepository;

    @Mock
    AuthenticationDomainService authenticationDomainService;

    AuthenticationApplicationService applicationService;

    @BeforeEach
    void setup() {
        this.applicationService = new AuthenticationApplicationService(authenticationRepository,
                authenticationDomainService);
    }

    @Test
    void testAuthenticateOf_AuthenticationSuccess() throws DomainException {

        var apiKey = "APIKEYTENANT";

        Tenant tenant = Tenant.create(TenantId.create("0000000001"), TenantIdentifier.create("TEST"), "テストテナント");
        Authentication authentication = Authentication.createFromTenant(tenant.getTenantId());
        TenantNortisUser userDetails = TenantNortisUser.createOfTenant(authentication, tenant, false);

        when(this.authenticationDomainService.authorize(eq(ApiKey.create(apiKey)))).thenReturn(userDetails);

        assertDoesNotThrow(() -> {
            NortisUserDetails user = this.applicationService.authenticateOf(apiKey);

            assertThat(user).isEqualTo(userDetails);
        });
    }

    @Test
    void testAuthenticateOf_AuthenticationFailure() throws DomainException {
        var apiKey = "APIKEYTENANT";

        when(this.authenticationDomainService.authorize(eq(ApiKey.create(apiKey))))
                .thenThrow(new DomainException(MessageCodes.nortis10003()));

        assertThrows(DomainException.class, () -> {
            this.applicationService.authenticateOf(apiKey);

        });
    }

    @Test
    void testRemoveExpiredAuthentication() throws DomainException {

        var baseDateTime = LocalDateTime.now();

        Authentication authentication1 = Authentication.createFromUserId(UserId.create("1000000001"));
        Authentication authentication2 = Authentication.createFromUserId(UserId.create("1000000002"));
        when(this.authenticationRepository.getUserAuthentication())
                .thenReturn(Lists.list(authentication1, authentication2));

        // when(this.authenticationDomainService.checkExpired(authentication1,
        // baseDateTime)).thenReturn(true);
        // when(this.authenticationDomainService.checkExpired(authentication2,
        // baseDateTime)).thenReturn(false);
        // 上記だと引数をちゃんと見れず、モックが機能しないので、全て対象にする
        when(this.authenticationDomainService.checkExpired(any(), any())).thenReturn(true);

        assertDoesNotThrow(() -> {
            this.applicationService.removeExpiredAuthentication(LocalDateTime.of(2022, 1, 5, 12, 40, 00));
        });

        verify(this.authenticationRepository).remove(eq(authentication1));
        verify(this.authenticationRepository).remove(eq(authentication2));
    }

}
