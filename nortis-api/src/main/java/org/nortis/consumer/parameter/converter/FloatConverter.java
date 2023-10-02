package org.nortis.consumer.parameter.converter;

import org.nortis.consumer.parameter.Converter;

/**
 * {@link Integer}に変換する{@link Converter}の実装クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class FloatConverter implements Converter<Float> {

    @Override
    public Float deserialize(String value) {
        return value != null ? Float.parseFloat(value) : null;
    }

    @Override
    public String serialize(Float value) {
        return value != null ? String.valueOf(value) : null;
    }

}
