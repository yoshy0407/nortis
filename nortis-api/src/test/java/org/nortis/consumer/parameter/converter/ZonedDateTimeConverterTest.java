package org.nortis.consumer.parameter.converter;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

class ZonedDateTimeConverterTest {

    ZonedDateTimeConverter converter = new ZonedDateTimeConverter(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    @Test
    void testDeserialize() {
        assertThat(converter.deserialize("1993-05-06T10:02:34+09:00"))
                .isEqualTo(ZonedDateTime.of(1993, 5, 6, 10, 2, 34, 0, ZoneId.of("Asia/Tokyo")));
        assertThat(converter.deserialize(null)).isNull();
    }

    @Test
    void testSerialize() {
        assertThat(converter.serialize(ZonedDateTime.of(1993, 4, 3, 10, 2, 34, 0, ZoneId.of("Asia/Tokyo"))))
                .isEqualTo("1993-04-03T10:02:34+09:00");
        assertThat(converter.serialize(null)).isNull();
    }

}
