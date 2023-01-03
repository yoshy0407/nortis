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

	/**
	 * 変換します
	 * @param data 変換元データ
	 * @return 変換後データ
	 */
	R translate(D data);
	
	/**
	 * 何も変換しない{@link ApplicationTranslator}を返却します
	 * @param <T> 元クラス
	 * @return 変換しない{@link ApplicationTranslator}
	 */
	static <T> ApplicationTranslator<T, Void> nothing() {
		return t -> null;
	}
}
