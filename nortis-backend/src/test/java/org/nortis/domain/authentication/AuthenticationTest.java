package org.nortis.domain.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;

class AuthenticationTest {

    @Test
    void testCreateFromTenant() throws DomainException {
        Authentication auth = Authentication.createFromTenant(TenantId.create("TEST1"));

        assertThat(auth.getApiKey()).isNotNull();
        assertThat(auth.getTenantId()).isEqualTo(TenantId.create("TEST1"));
        assertThat(auth.getUserId()).isNull();
    }

    @Test
    void testCreateFromTenant_TenantIdNull() throws DomainException {
        assertThrows(DomainException.class, () -> {
            Authentication.createFromTenant(null);
        });
    }

    @Test
    void testCreateFromUserId() throws DomainException {
        Authentication auth = Authentication.createFromUserId(UserId.create("23456"));

        assertThat(auth.getApiKey()).isNotNull();
        assertThat(auth.getTenantId()).isNull();
        assertThat(auth.getUserId()).isEqualTo(UserId.create("23456"));
    }

    @Test
    void testCreateFromUserId_UserIdNull() throws DomainException {
        assertThrows(DomainException.class, () -> {
            Authentication.createFromUserId(null);
        });
    }

}
