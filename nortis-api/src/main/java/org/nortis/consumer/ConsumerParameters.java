package org.nortis.consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.nortis.consumer.parameter.ParameterDefinition;

/**
 * パラメータを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ConsumerParameters {

    private final Map<String, String> map;

    /**
     * インスタンスを生成します
     */
    public ConsumerParameters() {
        this.map = new HashMap<>();
    }

    /**
     * インスタンスを生成します
     * 
     * @param map マップ
     */
    public ConsumerParameters(Map<String, String> map) {
        this.map = Collections.unmodifiableMap(map);
    }

    /**
     * パラメータに対応する値を取得します
     * 
     * @param <T>               パラメータの型
     * @param consumerParameter 定義されているパラメータ
     * @return パラメータの値
     */
    public <T> T getParameter(ParameterDefinition<T> consumerParameter) {
        String value = this.map.get(consumerParameter.getParameterName());
        return consumerParameter.deserialize(value);
    }

    /**
     * {@link Entry}の{@link Set}を返します
     * 
     * @return {@link Entry}の{@link Set}
     */
    public Set<Entry<String, String>> entrySet() {
        return this.map.entrySet();
    }
}
