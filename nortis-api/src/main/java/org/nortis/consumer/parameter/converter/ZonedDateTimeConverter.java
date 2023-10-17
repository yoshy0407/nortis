package org.nortis.consumer.parameter.converter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.nortis.consumer.parameter.Converter;

/**
 * {@link LocalDate}を変換する{@link Converter}
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ZonedDateTimeConverter implements Converter<ZonedDateTime> {

    private final DateTimeFormatter formatter;

    /**
     * インスタンスを生成します
     * 
     * @param formatter フォーマッター
     */
    public ZonedDateTimeConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public ZonedDateTime deserialize(String value) {
        return value != null ? ZonedDateTime.parse(value, formatter) : null;
    }

    @Override
    public String serialize(ZonedDateTime value) {
        return value != null ? value.format(formatter) : null;
    }

}
