package org.nortis.infrastructure.jpa;

import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link UUID}の{@link AttributeConverter}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convertToDatabaseColumn(UUID attribute) {
		return attribute.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID convertToEntityAttribute(String dbData) {
		return UUID.fromString(dbData);
	}

}
