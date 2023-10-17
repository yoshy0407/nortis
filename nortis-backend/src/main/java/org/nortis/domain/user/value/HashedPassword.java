package org.nortis.domain.user.value;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.Domain;

/**
 * ハッシュ化されたパスワード
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "create", accessorMethod = "toString")
@EqualsAndHashCode
public class HashedPassword implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String value;

    private HashedPassword(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * インスタンスを構築します
     * 
     * @param value 値
     * @return ハッシュ化されたパスワード
     * @throws DomainException バリデーションエラー
     */
    public static HashedPassword create(String value) {
        return new HashedPassword(value);
    }

}
