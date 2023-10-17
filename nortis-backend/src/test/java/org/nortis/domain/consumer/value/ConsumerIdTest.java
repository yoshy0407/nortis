package org.nortis.domain.consumer.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.infrastructure.exception.DomainException;

class ConsumerIdTest extends TestBase {

    @Test
    void testHashCodeEqual() throws DomainException {
        ConsumerId endpointId1 = ConsumerId.create("1000000001");
        ConsumerId endpointId2 = ConsumerId.create("1000000001");

        assertThat(endpointId1.hashCode()).isEqualTo(endpointId2.hashCode());
    }

    @Test
    void testHashCodeNotEqual() throws DomainException {
        ConsumerId endpointId1 = ConsumerId.create("1000000001");
        ConsumerId endpointId2 = ConsumerId.create("1000000002");

        assertThat(endpointId1.hashCode()).isNotEqualTo(endpointId2.hashCode());
    }

    @Test
    void testToString() throws DomainException {
        ConsumerId endpointId1 = ConsumerId.create("1000000001");

        assertThat(endpointId1.toString()).isEqualTo("1000000001");
    }

    @Test
    void testEquals() throws DomainException {
        ConsumerId endpointId1 = ConsumerId.create("1000000001");
        ConsumerId endpointId2 = ConsumerId.create("1000000001");

        assertThat(endpointId1.equals(endpointId2)).isTrue();
    }

    @Test
    void testNotEquals() throws DomainException {
        ConsumerId endpointId1 = ConsumerId.create("1000000001");
        ConsumerId endpointId2 = ConsumerId.create("1000099999");

        assertThat(endpointId1.equals(endpointId2)).isFalse();
    }

    @Test
    void testEmptyError() {
        assertThrows(DomainException.class, () -> {
            ConsumerId.create("");
        }, "コンシューマIDが未設定です");
    }

    @Test
    void testNullError() {
        assertThrows(DomainException.class, () -> {
            ConsumerId.create(null);
        }, "コンシューマIDが未設定です");
    }

}
