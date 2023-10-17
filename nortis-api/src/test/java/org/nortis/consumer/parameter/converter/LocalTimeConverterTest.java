package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class LocalTimeConverterTest {

    LocalTimeConverter converter = new LocalTimeConverter(DateTimeFormatter.ISO_LOCAL_TIME);

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("10:02:34")).isEqualTo(LocalTime.of(10, 2, 34));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(LocalTime.of(10, 2, 34))).isEqualTo("10:02:34");
        assertThat(converter.serialize(null)).isNull();
    }

}
