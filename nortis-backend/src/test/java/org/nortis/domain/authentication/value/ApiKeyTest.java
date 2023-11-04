package org.nortis.domain.authentication.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.exception.DomainException;

class ApiKeyTest {

    @Test
    void testHashCode() throws DomainException {
        ApiKey apiKey1 = ApiKey.create("TESTVALUE");
        ApiKey apiKey2 = ApiKey.create("TESTVALUE");
        ApiKey apiKey3 = ApiKey.create("TESTVALUE123");

        assertThat(apiKey1.hashCode()).isEqualTo(apiKey2.hashCode());
        assertThat(apiKey1.hashCode()).isNotEqualTo(apiKey3.hashCode());
    }

    @Test
    void testToString() throws DomainException {
        ApiKey apiKey1 = ApiKey.create("TESTVALUE");

        assertThat(apiKey1.toString()).isEqualTo("TESTVALUE");
    }

    @Test
    void testNewKey() throws DomainException {
        ApiKey apiKey = ApiKey.newKey();

        assertThat(apiKey.toString()).hasSize(36);
    }

    @Test
    void testCreate_nullError() {
        assertThrows(DomainException.class, () -> {
            ApiKey.create(null);
        });

    }

    @Test
    void testCreate_emptyError() {
        assertThrows(DomainException.class, () -> {
            ApiKey.create("");
        });

    }

    @Test
    void testCreate_lengthError() {
        assertThrows(DomainException.class, () -> {
            ApiKey.create("1234567890123456789012345678901234567");
        });

    }

    @Test
    void testEqualsObject() throws DomainException {
        ApiKey apiKey1 = ApiKey.create("TESTVALUE");
        ApiKey apiKey2 = ApiKey.create("TESTVALUE");
        ApiKey apiKey3 = ApiKey.create("TESTVALUE123");

        assertThat(apiKey1.equals(apiKey2)).isTrue();
        assertThat(apiKey1.equals(apiKey3)).isFalse();
    }

}
