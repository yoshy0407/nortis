package org.nortis.consumer.parameter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    @Test
    void testNoCheck() {
        var result = Validator.noCheck().validate("test");

        assertThat(result.isSuccess()).isTrue();
    }

}
