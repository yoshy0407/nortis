package org.nortis.consumer.parameter;

/**
 * コンバーターのインタフェースです
 * 
 * @author yoshiokahiroshi
 * @param <T> 対象の型
 * @version 1.0.0
 */
public interface Converter<T> {

    /**
     * 値をデシリアライズします
     * 
     * @param value 値
     * @return デシリアライズした値
     */
    T deserialize(String value);

    /**
     * 値をシリアライズします
     * 
     * @param value 値
     * @return シリアライズした値
     */
    String serialize(T value);

}
