package org.nortis.infrastructure.application;

/**
 * アプリケーション層のデータの変換インタフェースです
 * 
 * @author yoshiokahiroshi
 * @param <D> アプリケーションクラスのデータ
 * @param <R> 変換後のデータ
 * @version 1.0.0
 */
@FunctionalInterface
public interface ApplicationTranslator<D, R> {

	R translate(D data);
	
	default <T> ApplicationTranslator<T, R> nothing() {
		return T -> null;
	}
}
