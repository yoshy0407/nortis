package org.nortis.consumer.parameter.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.nortis.consumer.parameter.ValidationResult;
import org.nortis.consumer.parameter.Validator;

/**
 * 複数条件をチェックする{@link Validator}の実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class CompositeValidator implements Validator {

    private final List<Validation> validationList = new ArrayList<>();

    @Override
    public ValidationResult validate(String value) {
        for (Validation validation : this.validationList) {
            if (!validation.validate(value)) {
                return ValidationResult.failure(validation.errorMessage);
            }
        }
        return ValidationResult.success();
    }

    /**
     * バリデーションを追加します
     * 
     * @param function     チェックロジック
     * @param errorMessage エラーメッセージ
     */
    void add(Function<String, Boolean> function, String errorMessage) {
        this.validationList.add(new Validation(function, errorMessage));
    }

    private static class Validation {

        private final Function<String, Boolean> validation;

        private final String errorMessage;

        private Validation(Function<String, Boolean> validation, String errorMessage) {
            this.validation = validation;
            this.errorMessage = errorMessage;
        }

        boolean validate(String value) {
            return validation.apply(value);
        }
    }

}
