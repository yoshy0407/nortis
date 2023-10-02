package org.nortis.domain.user.value;

import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.security.Identity;
import org.seasar.doma.Domain;

/**
 * ユーザID
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public class UserId extends Identity {

    private UserId(String value) throws DomainException {
        super(value, "ユーザID");
    }

    /**
     * ユーザIDを作成します
     * 
     * @param value 文字列
     * @return ユーザID
     * @throws DomainException ドメインロジックエラー
     */
    public static UserId create(String value) throws DomainException {
        return new UserId(value);
    }

    /**
     * 新しい値を採番します
     * 
     * @param number 数字
     * @return {@link TenantId}
     * @throws DomainException ドメインロジックエラー
     */
    public static UserId createNew(long number) {
        try {
            return create(String.format("%010d", number));
        } catch (DomainException ex) {
            throw UnexpectedException.convertDomainException(ex);
        }
    }

    /**
     * Domaのファクトリメソッドです
     * 
     * @param value 文字列
     * @return ユーザID
     * @throws DomainException ドメインロジックエラー
     */
    public static UserId createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
