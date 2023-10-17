package org.nortis.infrastructure.security;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.validation.Validations;

/**
 * IDを表す抽象クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
public class Identity implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String value;

    /**
     * コンストラクター
     * 
     * @param value       値
     * @param displayName 表示名
     * @throws DomainException バリデーションエラー
     */
    public Identity(String value, String displayName) throws DomainException {
        Validations.hasText(value, displayName);
        Validations.maxTextLength(value, 10, displayName);
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
