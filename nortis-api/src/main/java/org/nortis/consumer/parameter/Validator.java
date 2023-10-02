package org.nortis.consumer.parameter;

/**
 * 値のバリデーターのインタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@FunctionalInterface
public interface Validator {

    /**
     * バリデーションを実施します
     * 
     * @param value 値
     * @return バリデーション結果
     */
    ValidationResult validate(String value);

    /**
     * チェックを行わないバリデーターを返します
     * 
     * @return バリデーター
     */
    static <T> Validator noCheck() {
        return v -> ValidationResult.success();
    }
}
