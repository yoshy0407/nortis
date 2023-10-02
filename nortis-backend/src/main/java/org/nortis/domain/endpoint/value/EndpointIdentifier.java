package org.nortis.domain.endpoint.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * エンドポイント識別子を表すクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public final class EndpointIdentifier {

    private static final String DISPLAY_NAME = "エンドポイント識別子";

    private final String value;

    private EndpointIdentifier(String value) throws DomainException {
        Validations.hasText(value, DISPLAY_NAME);
        Validations.maxTextLength(value, 20, DISPLAY_NAME);
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * {@link EndpointIdentifier}のファクトリメソッドです
     * 
     * @param value 値
     * @return {@link EndpointIdentifier}
     * @throws DomainException ドメインロジックエラー
     */
    public static EndpointIdentifier create(String value) throws DomainException {
        return new EndpointIdentifier(value);
    }

    /**
     * Domaのファクトリメソッドです
     * 
     * @param value 値
     * @return {@link EndpointIdentifier}
     */
    public static EndpointIdentifier createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
