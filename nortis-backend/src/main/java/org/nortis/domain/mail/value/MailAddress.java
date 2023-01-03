package org.nortis.domain.mail.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * メールアドレスを表すオブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "create", accessorMethod = "toString")
@EqualsAndHashCode
public final class MailAddress {

	/**
	 * メールアドレスの値
	 */
	private final String value;
	
	private MailAddress(final String value) {
		Validations.hasText(value, "メールアドレス");
		Validations.contains(value, "@", "MSG30001");
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
	 * メールアドレスを構築します
	 * @param mailAddress メールアドレス
	 * @return {@link MailAddress}
	 */
	public static MailAddress create(final String mailAddress) {
		return new MailAddress(mailAddress);
	}
	
}
