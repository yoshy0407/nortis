package org.nortis.domain.user.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * ユーザID
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public class UserId {

	private final String value;
	
	private UserId(String value) throws DomainException {
		Validations.hasText(value, "ユーザID");
		Validations.maxTextLength(value, 10, "ユーザID");
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
	 * ユーザIDを作成します
	 * @param value 文字列
	 * @return ユーザID
	 * @throws DomainException ドメインロジックエラー
	 */
	public static UserId create(String value) throws DomainException {
		return new UserId(value);
	}

	/**
	 * Domaのファクトリメソッドです
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
