package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class StringConverterTest {

    StringConverter converter = new StringConverter();

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("test")).isEqualTo("test");
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize("test")).isEqualTo("test");
        assertThat(converter.serialize(null)).isNull();
    }

}
