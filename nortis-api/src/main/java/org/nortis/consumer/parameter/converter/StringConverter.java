package org.nortis.consumer.parameter.converter;

import org.nortis.consumer.parameter.Converter;

/**
 * {@link String}に変換する{@link Converter}の実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class StringConverter implements Converter<String> {

    @Override
    public String deserialize(String value) {
        return value;
    }

    @Override
    public String serialize(String value) {
        return value;
    }

}
