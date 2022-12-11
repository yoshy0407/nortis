package org.nortis.domain.tenant.value;

import org.nortis.infrastructure.validation.Validations;
import lombok.EqualsAndHashCode;

/**
 * テナント省略名を表すクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
public class TenantId {

	private final String value;
	
	private TenantId(String value) {
		Validations.hasText(value, "テナントID");
		Validations.maxTextLength(value, 10, "テナントID");
		this.value = value;
	}
	
	/**
	 * {@link TenantId}のファクトリメソッドです
	 * @param value 値
	 * @return {@link TenantId}
	 */
	public static TenantId of(String value) {
		return new TenantId(value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
}
