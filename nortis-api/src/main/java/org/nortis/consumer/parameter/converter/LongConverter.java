package org.nortis.consumer.parameter.converter;

import org.nortis.consumer.parameter.Converter;

/**
 * {@link Long}に変換する{@link Converter}の実装クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class LongConverter implements Converter<Long> {

    @Override
    public Long deserialize(String value) {
        return value != null ? Long.parseLong(value) : null;
    }

    @Override
    public String serialize(Long value) {
        return value != null ? String.valueOf(value) : null;
    }

}
