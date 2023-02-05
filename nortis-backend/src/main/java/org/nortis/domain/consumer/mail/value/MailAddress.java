package org.nortis.domain.consumer.mail.value;

import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * メールアドレスを表すオブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "createOfDoma", accessorMethod = "toString")
@EqualsAndHashCode
public final class MailAddress {

	/**
	 * メールアドレスの値
	 */
	private final String value;
	
	private MailAddress(final String value) throws DomainException {
		Validations.hasText(value, "メールアドレス");
		Validations.contains(value, "@", MessageCodes.nortis30001());
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
	 * @throws DomainException ドメインロジックエラー
	 */
	public static MailAddress create(final String mailAddress) throws DomainException {
		return new MailAddress(mailAddress);
	}

	/**
	 * Domaのファクトリメソッドです
	 * @param mailAddress メールアドレス
	 * @return {@link MailAddress}
	 * @throws DomainException ドメインロジックエラー
	 */
	public static MailAddress createOfDoma(final String mailAddress) {
		try {
			return create(mailAddress);
		} catch (DomainException e) {
			throw UnexpectedException.convertDomainException(e);
		}
	}

}
