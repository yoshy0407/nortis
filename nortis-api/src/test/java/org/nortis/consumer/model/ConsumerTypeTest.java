package org.nortis.consumer.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.nortis.consumer.ApiMessageCode;

class ConsumerTypeTest {

    @Test
    void testConsumerType_Error() {
        assertThatThrownBy(() -> {
            new ConsumerType("123456789012345678901", "テスト");
        }).isInstanceOf(IllegalArgumentException.class).hasMessage(ApiMessageCode.NORTISAPI100001.getMessage(20));
    }

    @Test
    void testGetCode() {
        var consumerType = new ConsumerType("MAIL", "メール");
        assertThat(consumerType.getCode()).isEqualTo("MAIL");
    }

    @Test
    void testGetDisplayName() {
        var consumerType = new ConsumerType("MAIL", "メール");
        assertThat(consumerType.getDisplayName()).isEqualTo("メール");
    }

    @Test
    void testHashCode() {
        var consumertype1 = new ConsumerType("MAIL", "メール");
        var consumertype2 = new ConsumerType("MAIL", "メール");
        var consumertype3 = new ConsumerType("VALUE", "メール");

        assertThat(consumertype1.hashCode()).isEqualTo(consumertype2.hashCode());
        assertThat(consumertype1.hashCode()).isNotEqualTo(consumertype3.hashCode());
    }

    @Test
    void testEquals() {
        var consumertype1 = new ConsumerType("MAIL", "メール");
        var consumertype2 = new ConsumerType("MAIL", "メール");
        var consumertype3 = new ConsumerType("VALUE", "メール");

        assertThat(consumertype1.equals(consumertype2)).isTrue();
        assertThat(consumertype1.equals(consumertype3)).isFalse();
    }

}
