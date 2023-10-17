package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class LocalDateConverterTest {

    LocalDateConverter converter = new LocalDateConverter(DateTimeFormatter.BASIC_ISO_DATE);

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("19930506")).isEqualTo(LocalDate.of(1993, 5, 6));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(LocalDate.of(1995, 4, 3))).isEqualTo("19950403");
        assertThat(converter.serialize(null)).isNull();
    }

}
