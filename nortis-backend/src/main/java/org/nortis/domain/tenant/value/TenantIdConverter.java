package org.nortis.domain.tenant.value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link TenantId}の{@link AttributeConverter}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Converter(autoApply = true)
public class TenantIdConverter implements AttributeConverter<TenantId, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convertToDatabaseColumn(TenantId attribute) {
		return attribute.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TenantId convertToEntityAttribute(String dbData) {
		return TenantId.create(dbData);
	}

}
