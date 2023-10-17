package org.nortis.domain.tenant.value;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * ロールIDを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "createOfDoma", accessorMethod = "toString")
@EqualsAndHashCode
public class RoleId implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DISPLAY_NAME = "ロールID";

    private final String value;

    private RoleId(String value) throws DomainException {
        Validations.hasText(value, DISPLAY_NAME);
        Validations.maxTextLength(value, 5, DISPLAY_NAME);
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * {@link RoleId}のファクトリメソッドです
     * 
     * @param value 値
     * @return {@link RoleId}
     * @throws DomainException ドメインロジックエラー
     */
    public static RoleId create(String value) throws DomainException {
        return new RoleId(value);
    }

    /**
     * 新しい値を採番します
     * 
     * @param number 数字
     * @return {@link RoleId}
     * @throws DomainException ドメインロジックエラー
     */
    public static RoleId createNew(long number) {
        try {
            return create(String.format("%05d", number));
        } catch (DomainException ex) {
            throw UnexpectedException.convertDomainException(ex);
        }
    }

    /**
     * Domaのファクトリメソッドです
     * 
     * @param value 値
     * @return {@link TenantId}
     */
    public static RoleId createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
