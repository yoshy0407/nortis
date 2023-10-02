package org.nortis.domain.endpoint.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * サブジェクトテンプレート
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "createOfDoma", accessorMethod = "toString")
@EqualsAndHashCode
public class SubjectTemplate {

    private static final String DISPLAY_NAME = "サブジェクトテンプレート";

    private final String template;

    private SubjectTemplate(String template) throws DomainException {
        Validations.hasText(template, DISPLAY_NAME);
        Validations.maxTextLength(template, 100, DISPLAY_NAME);
        this.template = template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.template;
    }

    /**
     * 構築します
     * 
     * @param value 値
     * @return {@link SubjectTemplate}
     * @throws DomainException チェックエラー
     */
    public static SubjectTemplate create(String value) throws DomainException {
        return new SubjectTemplate(value);
    }

    /**
     * Domaから呼び出されます
     * 
     * @param value 値
     * @return {@link SubjectTemplate}
     */
    public static SubjectTemplate createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException ex) {
            throw UnexpectedException.convertDomainException(ex);
        }
    }
}
