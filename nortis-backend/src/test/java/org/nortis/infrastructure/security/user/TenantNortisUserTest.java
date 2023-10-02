package org.nortis.infrastructure.security.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.exception.DomainException;

class TenantNortisUserTest {

    TenantNortisUser user;

    Tenant tenant;

    Authentication authentication;

    @BeforeEach
    void setup() throws DomainException {
        var tenantId = TenantId.create("1000000001");
        this.tenant = Tenant.create(tenantId, TenantIdentifier.create("TEST"), "テストテナント");
        this.authentication = Authentication.createFromTenant(tenantId);
        this.user = TenantNortisUser.createOfTenant(authentication, tenant, false);
    }

    @Test
    void testIsAdmin() {
        assertThat(this.user.isAdmin()).isFalse();
    }

    @Test
    void testHasAuthorityOf() {
        assertThat(this.user.hasAuthorityOf(tenant, OperationId.READ_ENDPOINT)).isTrue();
    }

    @Test
    void testIsJoinTenantOf() throws DomainException {
        assertThat(this.user.isJoinTenantOf(TenantId.create("1000000001"))).isTrue();
    }

    @Test
    void testIsTenant() {
        assertThat(this.user.isTenant()).isTrue();
    }

    @Test
    void testIsUser() {
        assertThat(this.user.isUser()).isFalse();
    }

    @Test
    void testGetTenant() {
        assertThat(this.user.getTenant()).isEqualTo(this.tenant);
    }

    @Test
    void testGetAuthentication() {
        assertThat(this.user.getAuthentication()).isEqualTo(this.authentication);
    }

    @Test
    void testGetIdentity() throws DomainException {
        assertThat(this.user.getIdentity()).isEqualTo(TenantId.create("1000000001"));
    }

}
