package org.nortis.infrastructure.specification;

/**
 * フィルタイングする仕様パターンのインタフェースです
 * 
 * @author yoshiokahiroshi
 * @param <T> 対象オブジェクト
 * @version 2.0.0
 */
@FunctionalInterface
public interface SpecificationFilter<T> {

    /**
     * 仕様を満たすことをチェックします
     * 
     * @param obj チェック対象
     * @return 仕様を満たすかどうか
     */
    boolean isSatisfied(T obj);

}
