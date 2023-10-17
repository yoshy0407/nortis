package org.nortis.consumer.parameter;

import java.util.Optional;

/**
 * バリデーション結果を保持するクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ValidationResult {

    private final boolean success;

    private final String message;

    /**
     * コンストラクター
     * 
     * @param success 成功したかどうか
     * @param message メッセージ
     */
    protected ValidationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * 成功しているかどうか
     * 
     * @return 成功しているかどうか
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * メッセージ
     * 
     * @return メッセージ
     */
    public Optional<String> getMessage() {
        return Optional.ofNullable(this.message);
    }

    /**
     * 処理成功のオブジェクトを返します
     * 
     * @return 処理成功のオブジェクト
     */
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    /**
     * 処理失敗のオブジェクトを返します
     * 
     * @param message エラーメッセージ
     * @return 処理失敗のオブジェクト
     */
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
}
