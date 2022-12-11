package org.nortis.infrastructure.jpa;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link LocalDateTime}の{@link AttributeConverter}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
		return attribute == null ? null : Timestamp.valueOf(attribute);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
		return dbData == null ? null : dbData.toLocalDateTime();
	}

}
