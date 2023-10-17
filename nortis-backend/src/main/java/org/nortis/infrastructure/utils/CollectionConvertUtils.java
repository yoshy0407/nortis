package org.nortis.infrastructure.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * コレクションの変換に関するユーティリティクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class CollectionConvertUtils {

    /**
     * {@link Stream}を使って、{@link List}に変換します
     * 
     * @param <E> エンティティクラス
     * @param map {@link Map}
     * @return {@link List}
     */
    public static <E> List<E> toList(Map<?, E> map) {
        //@formatter:off
        return map.entrySet().stream()
                .map(entry -> entry.getValue())
                .toList();
        //@formatter:on
    }
}
