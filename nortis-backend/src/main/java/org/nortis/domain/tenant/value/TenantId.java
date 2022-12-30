package org.nortis.domain.tenant.value;

import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * テナント省略名を表すクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
@EqualsAndHashCode
public final class TenantId {

	/**
	 * 値
	 */
	@Getter
	private final String tenantId;
	
	private TenantId(String value) {
		Validations.hasText(value, "テナントID");
		Validations.maxTextLength(value, 10, "テナントID");
		this.tenantId = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.tenantId;
	}
	
	/**
	 * {@link TenantId}のファクトリメソッドです
	 * @param value 値
	 * @return {@link TenantId}
	 */
	public static TenantId create(String value) {
		return new TenantId(value);
	}

}
