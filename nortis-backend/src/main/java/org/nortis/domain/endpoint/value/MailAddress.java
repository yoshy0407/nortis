package org.nortis.domain.endpoint.value;

import org.nortis.infrastructure.validation.Validations;

import lombok.EqualsAndHashCode;

/**
 * メールアドレスを表すオブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
public final class MailAddress {

	/**
	 * メールアドレスの値
	 */
	private final String value;
	
	private MailAddress(final String value) {
		Validations.hasText(value, "メールアドレス");
		Validations.contains(value, "@", "MSG20001");
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
