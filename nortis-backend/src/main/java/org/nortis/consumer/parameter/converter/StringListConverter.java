package org.nortis.consumer.parameter.converter;

import java.util.List;
import java.util.stream.Stream;
import org.nortis.consumer.parameter.Converter;

/**
 * カンマ区切りの文字列を{@link List}に変換する{@link Converter}の実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class StringListConverter implements Converter<List<String>> {

    @Override
    public List<String> deserialize(String value) {
        return value != null ? Stream.of(value.split(",")).map(s -> s.trim()).toList() : null;
    }

    @Override
    public String serialize(List<String> value) {
        return value != null ? String.join(",", value) : null;
    }

}
