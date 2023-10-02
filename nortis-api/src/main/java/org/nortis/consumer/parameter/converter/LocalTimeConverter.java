package org.nortis.consumer.parameter.converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.nortis.consumer.parameter.Converter;

/**
 * {@link LocalTime}を変換する{@link Converter}
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class LocalTimeConverter implements Converter<LocalTime> {

    private final DateTimeFormatter formatter;

    /**
     * インスタンスを生成します
     * 
     * @param formatter フォーマッター
     */
    public LocalTimeConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalTime deserialize(String value) {
        return LocalTime.parse(value, formatter);
    }

    @Override
    public String serialize(LocalTime value) {
        return value.format(formatter);
    }

}
