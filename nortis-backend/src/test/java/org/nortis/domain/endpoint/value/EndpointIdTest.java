package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class EndpointIdTest extends TestBase {

    @Test
    void testHashCodeEqual() throws DomainException {
        EndpointId endpointId1 = EndpointId.create("00001");
        EndpointId endpointId2 = EndpointId.create("00001");

        assertThat(endpointId1.hashCode()).isEqualTo(endpointId2.hashCode());
    }

    @Test
    void testHashCodeNotEqual() throws DomainException {
        EndpointId endpointId1 = EndpointId.create("00001");
        EndpointId endpointId2 = EndpointId.create("00002");

        assertThat(endpointId1.hashCode()).isNotEqualTo(endpointId2.hashCode());
    }

    @Test
    void testToString() throws DomainException {
        EndpointId endpointId1 = EndpointId.create("00001");

        assertThat(endpointId1.toString()).isEqualTo("00001");
    }

    @Test
    void testEquals() throws DomainException {
        EndpointId endpointId1 = EndpointId.create("00001");
        EndpointId endpointId2 = EndpointId.create("00001");

        assertThat(endpointId1.equals(endpointId2)).isTrue();
    }

    @Test
    void testNotEquals() throws DomainException {
        EndpointId endpointId1 = EndpointId.create("00001");
        EndpointId endpointId2 = EndpointId.create("99999");

        assertThat(endpointId1.equals(endpointId2)).isFalse();
    }

    @Test
    void testEmptyError() {
        assertThrows(DomainException.class, () -> {
            EndpointId.create("");
        }, "エンドポイントIDが未設定です");
    }

    @Test
    void testNullError() {
        assertThrows(DomainException.class, () -> {
            EndpointId.create(null);
        }, "エンドポイントIDが未設定です");
    }

}
