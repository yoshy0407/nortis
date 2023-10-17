package org.nortis.domain.authentication.value;

import java.io.Serializable;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * APIキー
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String value;

    private ApiKey(String value) throws DomainException {
        Validations.hasText(value, "APIキー");
        Validations.maxTextLength(value, 36, "APIキー");
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * APIキーを構築します
     * 
     * @param value 文字列
     * @return APIキー
     * @throws DomainException ドメインロジックエラー
     */
    public static ApiKey create(String value) throws DomainException {
        return new ApiKey(value);
    }

    /**
     * APIキーを新規生成します
     * 
     * @return APIキー
     * @throws DomainException ドメインロジックエラー
     */
    public static ApiKey newKey() throws DomainException {
        return new ApiKey(UUID.randomUUID().toString());
    }

    /**
     * APIキーを構築します
     * 
     * @param value 文字列
     * @return APIキー
     * @throws DomainException ドメインロジックエラー
     */
    public static ApiKey createOfDoma(String value) {
        try {
            return create(value);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
