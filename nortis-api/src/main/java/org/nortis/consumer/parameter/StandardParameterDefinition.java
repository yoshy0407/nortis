package org.nortis.consumer.parameter;

/**
 * コンシューマのパラメータクラスです
 * 
 * @author yoshiokahiroshi
 * @param <T> パラメータの型
 * @version 1.0.0
 */
public class StandardParameterDefinition<T> extends AbstractParameterDefinition<T> {

    private final Converter<T> converter;

    private final Validator validator;

    /**
     * インスタンスを生成します
     * 
     * @param parameterName パラメータ名
     * @param displayName   画面表示名
     * @param require       必須かどうか
     * @param converter     コンバーター
     * @param validator     バリデーター
     */
    public StandardParameterDefinition(String parameterName, String displayName, boolean require,
            Converter<T> converter, Validator validator) {
        super(parameterName, displayName, require);
        this.converter = converter;
        this.validator = validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String serialize(T value) {
        return this.converter.serialize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T deserialize(String value) {
        return this.converter.deserialize(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidationResult validate(String value) {
        if (isRequire()) {
            if (value == null) {
                return ValidationResult.failure("$sが未設定です".formatted(getDisplayName()));
            }
        }
        return this.validator.validate(value);
    }

    /**
     * 作成します
     * 
     * @param <T>           パラメータの型
     * @param parameterName パラメータ名
     * @param displayName   画面の表示名
     * @param require       必須かどうか
     * @param converter     コンバーター
     * @return {@link StandardParameterDefinition}
     */
    public static <T> ParameterDefinition<T> ofNoValidation(String parameterName, String displayName, boolean require,
            Converter<T> converter) {
        return new StandardParameterDefinition<>(parameterName, displayName, require, converter, Validator.noCheck());
    }

}
