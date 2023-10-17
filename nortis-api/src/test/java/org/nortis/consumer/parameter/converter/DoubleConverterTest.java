package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class DoubleConverterTest {

    DoubleConverter converter = new DoubleConverter();

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("1.1")).isEqualTo(Double.parseDouble("1.1"));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(Double.valueOf(1.1D))).isEqualTo("1.1");
        assertThat(converter.serialize(null)).isNull();
    }

}
