package org.nortis.consumer.parameter.validator;

import java.util.function.Function;

/**
 * バリデータービルダーの抽象クラスです
 * 
 * @author yoshiokahiroshi
 * @param <T> 対象の型
 * @param <B> ビルダー
 * @version 1.0.0
 */
public class AbstractValidatorBuilder<B extends AbstractValidatorBuilder<B>> {

    private final CompositeValidator validator = new CompositeValidator();

    /**
     * nullでないことをチェックします
     * 
     * @param errorMessage エラーメッセージ
     * @return このインスタンス
     */
    @SuppressWarnings("unchecked")
    public B notNull(String errorMessage) {
        add(s -> s != null, errorMessage);
        return (B) this;
    }

    /**
     * 独自のチェック処理を追加します
     * 
     * @param function チェック処理
     * @param message  エラーメッセージ
     * @return ビルダー
     */
    @SuppressWarnings("unchecked")
    public B add(Function<String, Boolean> function, String message) {
        this.validator.add(function, message);
        return (B) this;
    }

    /**
     * 構築します
     * 
     * @return {@link CompositeValidator}
     */
    public CompositeValidator build() {
        return validator;
    }
}
