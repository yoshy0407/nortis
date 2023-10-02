package org.nortis.domain.tenant.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class RoleIdTest extends TestBase {

    @Test
    void testCreate_IsNull() throws DomainException {
        assertThrows(DomainException.class, () -> {
            RoleId.create(null);
        });
    }

    @Test
    void testCreate_IsBlank() throws DomainException {
        assertThrows(DomainException.class, () -> {
            RoleId.create("");
        });
    }

    @Test
    void testCreate_IsTooMuchLength() throws DomainException {
        assertThrows(DomainException.class, () -> {
            RoleId.create("000001");
        });
    }

    @Test
    void testHashCode_IsTrue() throws DomainException {
        assertThat(RoleId.create("00001").hashCode()).isEqualTo(RoleId.createNew(1).hashCode());
    }

    @Test
    void testHashCode_IsFalse() throws DomainException {
        assertThat(RoleId.create("00001").hashCode()).isNotEqualTo(RoleId.createNew(2).hashCode());
    }

    @Test
    void testEquals_IsTrue() throws DomainException {
        assertThat(RoleId.create("00001").equals(RoleId.create("00001"))).isTrue();
    }

    @Test
    void testEquals_IsFalse() throws DomainException {
        assertThat(RoleId.create("00001").equals(RoleId.create("00002"))).isFalse();
    }

    @Test
    void testToString() throws DomainException {
        assertThat(RoleId.create("00001").toString()).isEqualTo("00001");
    }

}
