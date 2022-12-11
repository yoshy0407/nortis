package org.nortis.domain.endpoint.value;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * {@link EndpointId}の{@link AttributeConverter}です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Converter(autoApply = true)
public class EndpointIdConverter implements AttributeConverter<EndpointId, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String convertToDatabaseColumn(EndpointId attribute) {
		return attribute.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EndpointId convertToEntityAttribute(String dbData) {
		return EndpointId.of(dbData);
	}

}
