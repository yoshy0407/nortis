package org.nortis.domain.tenant.value;

import java.io.Serializable;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.security.Identity;
import org.seasar.doma.Domain;

/**
 * テナント省略名を表すクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public final class TenantId extends Identity implements Serializable {

    private static final long serialVersionUID = 1L;

    private TenantId(String value) throws DomainException {
        super(value, "テナントID");
    }

    /**
     * {@link TenantId}のファクトリメソッドです
     * 
     * @param value 値
     * @return {@link TenantId}
     * @throws DomainException ドメインロジックエラー
     */
    public static TenantId create(String value) throws DomainException {
        return new TenantId(value);
    }

    /**
     * 新しい値を採番します
     * 
     * @param number 数字
     * @return {@link TenantId}
     * @throws DomainException ドメインロジックエラー
     */
    public static TenantId createNew(long number) {
        try {
            return create(String.format("%010d", number));
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
    public static TenantId createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
