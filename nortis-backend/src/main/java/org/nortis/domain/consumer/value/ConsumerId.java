package org.nortis.domain.consumer.value;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * エンドポイントIDの値オブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "createOfDoma", accessorMethod = "toString")
@EqualsAndHashCode
public final class ConsumerId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * エンドポイントIDの値
     */
    private final String value;

    /**
     * コンストラクター
     * 
     * @param value 値
     * @throws DomainException ドメインロジックエラー
     */
    private ConsumerId(final String value) throws DomainException {
        Validations.hasText(value, "エンドポイントID");
        Validations.maxTextLength(value, 10, "エンドポイントID");
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * ファクトリメソッドです
     * 
     * @param value 値
     * @return {@link ConsumerId}
     * @throws DomainException ドメインロジックエラー
     */
    public static ConsumerId create(final String value) throws DomainException {
        return new ConsumerId(value);
    }

    /**
     * 新しい値を採番します
     * 
     * @param number 新しい値
     * @return エンドポイントID
     */
    public static ConsumerId createNew(long number) {
        try {
            return ConsumerId.create(String.format("%010d", number));
        } catch (DomainException ex) {
            throw UnexpectedException.convertDomainException(ex);
        }
    }

    /**
     * Domaのファクトリメソッドです
     * 
     * @param value 値
     * @return {@link ConsumerId}
     * @throws DomainException ドメインロジックエラー
     */
    public static ConsumerId createOfDoma(final String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
