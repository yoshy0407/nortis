package org.nortis.consumer.parameter.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.nortis.consumer.parameter.Converter;

/**
 * {@link LocalDateTime}を変換する{@link Converter}
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    private final DateTimeFormatter formatter;

    /**
     * インスタンスを生成します
     * 
     * @param formatter フォーマッター
     */
    public LocalDateTimeConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalDateTime deserialize(String value) {
        return LocalDateTime.parse(value, formatter);
    }

    @Override
    public String serialize(LocalDateTime value) {
        return value.format(formatter);
    }

}
