package org.nortis.domain.endpoint.value;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;


/**
 * エンドポイントIDの値オブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "createOfDoma", accessorMethod = "toString")
@EqualsAndHashCode
public final class EndpointId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * エンドポイントIDの値
	 */
	private final String value;
	
	/**
	 * コンストラクター
	 * @param value 値
	 * @throws DomainException ドメインロジックエラー
	 */
	private EndpointId(final String value) throws DomainException {
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
	 * @param value 値
	 * @return {@link EndpointId}
	 * @throws DomainException ドメインロジックエラー
	 */
	public static EndpointId create(final String value) throws DomainException {
		return new EndpointId(value);
	}

	/**
	 * Domaのファクトリメソッドです
	 * @param value 値
	 * @return {@link EndpointId}
	 * @throws DomainException ドメインロジックエラー
	 */
	public static EndpointId createOfDoma(final String value) {
		try {
			return create(value);
		} catch (DomainException e) {
			throw UnexpectedException.convertDomainException(e);
		}
	}

}
