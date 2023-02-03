package org.nortis.domain.user.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * ユーザID
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public class UserId {

	private final String value;
	
	private UserId(String value) {
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
	 */
	public static UserId create(String value) {
		return new UserId(value);
	}
	
}
