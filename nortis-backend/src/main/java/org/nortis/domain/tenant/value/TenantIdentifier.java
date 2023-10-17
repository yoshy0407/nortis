package org.nortis.domain.tenant.value;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * テナント省略名を表すクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public final class TenantIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME = "テナント識別子";

    private final String value;

    private TenantIdentifier(String value) throws DomainException {
        Validations.hasText(value, DISPLAY_NAME);
        Validations.maxTextLength(value, 10, DISPLAY_NAME);
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * {@link TenantIdentifier}のファクトリメソッドです
     * 
     * @param value 値
     * @return {@link TenantIdentifier}
     * @throws DomainException ドメインロジックエラー
     */
    public static TenantIdentifier create(String value) throws DomainException {
        return new TenantIdentifier(value);
    }

    /**
     * Domaのファクトリメソッドです
     * 
     * @param value 値
     * @return {@link TenantIdentifier}
     */
    public static TenantIdentifier createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
