package org.nortis.domain.tenant.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class TenantIdentifierTest extends TestBase {

    @Test
    void testCreate_IsNull() {
        assertThrows(DomainException.class, () -> {
            TenantIdentifier.create(null);
        });
    }

    @Test
    void testCreate_IsBlank() {
        assertThrows(DomainException.class, () -> {
            TenantIdentifier.create("");
        });
    }

    @Test
    void testCreate_IsTooMuchLength() {
        assertThrows(DomainException.class, () -> {
            TenantIdentifier.create("12345678901");
        });
    }

    @Test
    void testHashCode_IsTrue() throws DomainException {
        assertThat(TenantIdentifier.create("TEST")).isEqualTo(TenantIdentifier.create("TEST"));
    }

    @Test
    void testHashCode_IsFalse() throws DomainException {
        assertThat(TenantIdentifier.create("TEST")).isNotEqualTo(TenantIdentifier.create("TEST1"));
    }

    @Test
    void testToString() throws DomainException {
        assertThat(TenantIdentifier.create("TEST").toString()).isEqualTo("TEST");
    }

    @Test
    void testEquals_IsTrue() throws DomainException {
        assertThat(TenantIdentifier.create("TEST").equals(TenantIdentifier.create("TEST"))).isTrue();
    }

    @Test
    void testEquals_IsFalse() throws DomainException {
        assertThat(TenantIdentifier.create("TEST").equals(TenantIdentifier.create("TEST123"))).isFalse();
    }

}
