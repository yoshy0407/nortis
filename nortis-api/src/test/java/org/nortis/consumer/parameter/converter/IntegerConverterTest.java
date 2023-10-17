package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class IntegerConverterTest {

    IntegerConverter converter = new IntegerConverter();

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("1")).isEqualTo(Integer.valueOf(1));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(Integer.valueOf(1))).isEqualTo("1");
        assertThat(converter.serialize(null)).isNull();
    }

}
