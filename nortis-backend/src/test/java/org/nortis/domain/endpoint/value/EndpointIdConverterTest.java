package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EndpointIdConverterTest {

	EndpointIdConverter converter = new EndpointIdConverter();
	
	@Test
	void testConvertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(EndpointId.create("TEST"))).isEqualTo("TEST");
	}

	@Test
	void testConvertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute("TEST")).isEqualTo(EndpointId.create("TEST"));
	}

}
