package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class LocalDateTimeConverterTest {

    LocalDateTimeConverter converter = new LocalDateTimeConverter(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("1993-05-06T10:02:34")).isEqualTo(LocalDateTime.of(1993, 5, 6, 10, 2, 34));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(LocalDateTime.of(1993, 4, 3, 10, 2, 34))).isEqualTo("1993-04-03T10:02:34");
        assertThat(converter.serialize(null)).isNull();
    }

}
