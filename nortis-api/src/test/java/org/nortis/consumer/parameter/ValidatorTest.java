package org.nortis.consumer.parameter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

class ValidatorTest {

    @Test
    void testNoCheck() {
        var result = Validator.noCheck().validate("test");

        assertThat(result.isSuccess()).isTrue();
    }

    @Property
    void testNoCheck_PropertyConstant(@ForAll String value) {
        var result = Validator.noCheck().validate(value);

        assertThat(result.isSuccess()).isTrue();
    }
}
