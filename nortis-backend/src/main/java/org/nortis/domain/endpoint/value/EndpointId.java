package org.nortis.domain.endpoint.value;

import org.nortis.infrastructure.validation.Validations;

import lombok.EqualsAndHashCode;

/**
 * エンドポイントIDの値オブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
public class EndpointId {

	private final String value;
	
	private EndpointId(String value) {
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
	 */
	public static EndpointId of(String value) {
		return new EndpointId(value);
	}
	
}
