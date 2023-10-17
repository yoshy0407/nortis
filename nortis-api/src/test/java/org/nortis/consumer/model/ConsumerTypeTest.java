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

}
