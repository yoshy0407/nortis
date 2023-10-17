package org.nortis.consumer.parameter.converter;

import org.nortis.consumer.parameter.Converter;

/**
 * {@link Integer}に変換する{@link Converter}の実装クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class IntegerConverter implements Converter<Integer> {

    @Override
    public Integer deserialize(String value) {
        return value != null ? Integer.parseInt(value) : null;
    }

    @Override
    public String serialize(Integer value) {
        return value != null ? String.valueOf(value) : null;
    }

}
