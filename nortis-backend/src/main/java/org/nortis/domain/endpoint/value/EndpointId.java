package org.nortis.domain.endpoint.value;

import java.io.Serializable;

import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

import lombok.EqualsAndHashCode;

/**
 * エンドポイントIDの値オブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, factoryMethod = "create", accessorMethod = "toString")
@EqualsAndHashCode
public final class EndpointId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * エンドポイントIDの値
	 */
	private final String endpointId;
	
	private EndpointId(final String endpointId) {
		Validations.hasText(endpointId, "エンドポイントID");
		Validations.maxTextLength(endpointId, 10, "エンドポイントID");
		this.endpointId = endpointId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.endpointId;
	}
	
	/**
	 * ファクトリメソッドです
	 * @param value 値
	 * @return {@link EndpointId}
	 */
	public static EndpointId create(final String value) {
		return new EndpointId(value);
	}
	
}
