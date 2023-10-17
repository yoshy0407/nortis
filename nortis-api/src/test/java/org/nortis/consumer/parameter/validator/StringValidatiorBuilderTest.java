package org.nortis.consumer.parameter.validator;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class StringValidatiorBuilderTest {

    @Test
    void testNotNull() {
        var validator = new StringValidatiorBuilder().notNull("error").build();
        var result = validator.validate(null);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("error");

        var result2 = validator.validate("text");
        assertThat(result2.isSuccess()).isTrue();
        assertThat(result2.getMessage()).isEmpty();
    }

    @Test
    void testNotBlank() {
        var validator = new StringValidatiorBuilder().notBlank("error").build();
        var result = validator.validate("");
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).contains("error");

        var result2 = validator.validate(null);
        assertThat(result2.isSuccess()).isFalse();
        assertThat(result2.getMessage()).contains("error");

        var result3 = validator.validate("text");
        assertThat(result3.isSuccess()).isTrue();
        assertThat(result3.getMessage()).isEmpty();
    }

    @Test
    void testEqualsLength() {
        var validator = new StringValidatiorBuilder().equalsLength(5, "error").build();
        var result1 = validator.validate("123456");

        assertThat(result1.isSuccess()).isFalse();
        assertThat(result1.getMessage()).contains("error");

        var result2 = validator.validate("12345");
        assertThat(result2.isSuccess()).isTrue();
        assertThat(result2.getMessage()).isEmpty();
    }

    @Test
    void testLessThanLength() {
        var validator = new StringValidatiorBuilder().lessThanLength(5, "error").build();
        var result1 = validator.validate("12345");

        assertThat(result1.isSuccess()).isFalse();
        assertThat(result1.getMessage()).contains("error");

        var result2 = validator.validate("1234");
        assertThat(result2.isSuccess()).isTrue();
        assertThat(result2.getMessage()).isEmpty();
    }

    @Test
    void testLessThanEqualsLength() {
        var validator = new StringValidatiorBuilder().lessThanEqualsLength(5, "error").build();
        var result1 = validator.validate("123456");

        assertThat(result1.isSuccess()).isFalse();
        assertThat(result1.getMessage()).contains("error");

        var result2 = validator.validate("12345");
        assertThat(result2.isSuccess()).isTrue();
        assertThat(result2.getMessage()).isEmpty();
    }

    @Test
    void testRegexPatternString() {
        var validator = new StringValidatiorBuilder().regex(Pattern.compile("[0-9]{3}"), "error").build();

        var result1 = validator.validate("12");

        assertThat(result1.isSuccess()).isFalse();
        assertThat(result1.getMessage()).contains("error");

        var result2 = validator.validate("123");

        assertThat(result2.isSuccess()).isTrue();
        assertThat(result2.getMessage()).isEmpty();
    }

    @Test
    void testRegexStringString() {
        var validator = new StringValidatiorBuilder().regex("[0-9]{3}", "error").build();

        var result1 = validator.validate("12");

        assertThat(result1.isSuccess()).isFalse();
        assertThat(result1.getMessage()).contains("error");

        var result2 = validator.validate("123");

        assertThat(result2.isSuccess()).isTrue();
        assertThat(result2.getMessage()).isEmpty();
    }

}
