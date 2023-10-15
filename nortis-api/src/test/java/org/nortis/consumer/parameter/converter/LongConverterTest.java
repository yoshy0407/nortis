package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class LongConverterTest {

    LongConverter converter = new LongConverter();

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("100")).isEqualTo(Long.valueOf(100));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(Long.valueOf(100))).isEqualTo("100");
        assertThat(converter.serialize(null)).isNull();
    }

}
