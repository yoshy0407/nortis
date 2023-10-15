package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class FloatConverterTest {

    FloatConverter converter = new FloatConverter();

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("1.11")).isEqualTo(Float.valueOf(1.11F));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(Float.valueOf(1.11F))).isEqualTo("1.11");
        assertThat(converter.serialize(null)).isNull();
    }

}
