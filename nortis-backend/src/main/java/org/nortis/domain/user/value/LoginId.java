package org.nortis.domain.user.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * ログインIDの値オブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public class LoginId {

	private static final String DISPLAY_NAME = "ログインID";
	
	private final String value;
	
	private LoginId(String value) throws DomainException {
		Validations.hasText(value, DISPLAY_NAME);
		Validations.maxTextLength(value, 30, DISPLAY_NAME);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	/**
	 * インスタンスを構築します
	 * @param value 値
	 * @return ログインID
	 * @throws DomainException バリデーションエラー
	 */
	public static LoginId create(String value) throws DomainException {
		return new LoginId(value);
	}
	
	/**
	 * インスタンスを構築します（Doma用）
	 * @param value 値
	 * @return ログインID
	 */
	public static LoginId createOfDoma(String value) {
		try {
			return create(value);
		} catch (DomainException ex) {
			throw UnexpectedException.convertDomainException(ex);
		}
	}
	
}
