package org.nortis.consumer.parameter.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.nortis.consumer.parameter.Converter;

/**
 * {@link LocalDate}を変換する{@link Converter}
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class LocalDateConverter implements Converter<LocalDate> {

    private final DateTimeFormatter formatter;

    /**
     * インスタンスを生成します
     * 
     * @param formatter フォーマッター
     */
    public LocalDateConverter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalDate deserialize(String value) {
        return value != null ? LocalDate.parse(value, formatter) : null;
    }

    @Override
    public String serialize(LocalDate value) {
        return value != null ? value.format(formatter) : null;
    }

}
