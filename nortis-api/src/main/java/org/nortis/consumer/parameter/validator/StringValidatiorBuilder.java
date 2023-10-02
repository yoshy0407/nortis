package org.nortis.consumer.parameter.validator;

import java.util.regex.Pattern;

/**
 * {@link String}のバリデータのビルダーです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class StringValidatiorBuilder extends AbstractValidatorBuilder<StringValidatiorBuilder> {

    /**
     * ブランクでないことをチェックします
     * 
     * @param errorMessage エラーメッセージ
     * @return ビルダー
     */
    public StringValidatiorBuilder notBlank(String errorMessage) {
        add(s -> s != null && s != "", errorMessage);
        return this;
    }

    /**
     * 文字数が一致することを確認します
     * 
     * @param length       文字数
     * @param errorMessage エラーメッセージ
     * @return ビルダー
     */
    public StringValidatiorBuilder equalsLength(int length, String errorMessage) {
        add(value -> value.length() == length, errorMessage);
        return this;
    }

    /**
     * 文字数以下であることをチェックします
     * 
     * @param length       文字数
     * @param errorMessage エラーメッセージ
     * @return ビルダー
     */
    public StringValidatiorBuilder lessThanLength(int length, String errorMessage) {
        add(value -> value.length() < length, errorMessage);
        return this;
    }

    /**
     * 文字数以下であることをチェックします
     * 
     * @param length       文字数
     * @param errorMessage エラーメッセージ
     * @return ビルダー
     */
    public StringValidatiorBuilder lessThanEqualsLength(int length, String errorMessage) {
        add(value -> value.length() <= length, errorMessage);
        return this;
    }

    /**
     * 正規表現でチェックを実施します
     * 
     * @param regex        正規表現
     * @param errorMessage エラーメッセージ
     * @return ビルダー
     */
    public StringValidatiorBuilder regex(Pattern regex, String errorMessage) {
        add(value -> regex.matcher(value).find(), errorMessage);
        return this;
    }

    /**
     * 正規表現でチェックを実施します
     * 
     * @param regex        正規表現
     * @param errorMessage エラーメッセージ
     * @return ビルダー
     */
    public StringValidatiorBuilder regex(String regex, String errorMessage) {
        return regex(Pattern.compile(regex), errorMessage);
    }

}
