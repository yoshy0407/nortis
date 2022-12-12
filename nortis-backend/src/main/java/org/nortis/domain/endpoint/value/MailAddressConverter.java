package org.nortis.domain.endpoint.value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link MailAddress}の{@link AttributeConverter}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Converter(autoApply = true)
public class MailAddressConverter implements AttributeConverter<MailAddress, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convertToDatabaseColumn(final MailAddress attribute) {
		return attribute.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MailAddress convertToEntityAttribute(final String dbData) {
		return MailAddress.create(dbData);
	}

}
