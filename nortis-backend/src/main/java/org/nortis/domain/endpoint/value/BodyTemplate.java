package org.nortis.domain.endpoint.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * ボディテンプレート
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "createOfDoma", accessorMethod = "toString")
@EqualsAndHashCode
public class BodyTemplate {

    private static final String DISPLAY_NAME = "ボディテンプレート";

    private final String template;

    private BodyTemplate(String template) throws DomainException {
        Validations.hasText(template, DISPLAY_NAME);
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
     * @return {@link BodyTemplate}
     * @throws DomainException チェックエラー
     */
    public static BodyTemplate create(String value) throws DomainException {
        return new BodyTemplate(value);
    }

    /**
     * Domaから呼び出されます
     * 
     * @param value 値
     * @return {@link BodyTemplate}
     */
    public static BodyTemplate createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException ex) {
            throw UnexpectedException.convertDomainException(ex);
        }
    }
}
