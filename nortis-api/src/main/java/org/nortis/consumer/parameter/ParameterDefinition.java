package org.nortis.consumer.parameter;

/**
 * コンシューマで使用するパラメータを表すインタフェースです
 * 
 * @author yoshiokahiroshi
 * @param <T> このパラメータで扱う型
 * @version 1.0.0
 */
public interface ParameterDefinition<T> {

    /**
     * パラメータ名を返します
     * 
     * @return パラメータ名
     */
    String getParameterName();

    /**
     * 画面表示名
     * 
     * @return 画面表示名
     */
    String getDisplayName();

    /**
     * 必須パラメータかどうか
     * 
     * @return 必須パラメータかどうか
     */
    boolean isRequire();

    /**
     * 文字列へのシリアライズ処理
     * 
     * @param value 値
     * @return 変換結果
     */
    String serialize(T value);

    /**
     * 文字列へのデシリアライズ処理
     * 
     * @param value 値
     * @return 変換結果
     */
    T deserialize(String value);

    /**
     * バリデーションを行います
     * 
     * @param value 値
     * @return バリデーション結果
     */
    ValidationResult validate(String value);

}