package org.nortis.consumer.parameter.converter;

import org.nortis.consumer.parameter.Converter;

/**
 * {@link Integer}に変換する{@link Converter}の実装クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DoubleConverter implements Converter<Double> {

    @Override
    public Double deserialize(String value) {
        return value != null ? Double.parseDouble(value) : null;
    }

    @Override
    public String serialize(Double value) {
        return value != null ? String.valueOf(value) : null;
    }

}
