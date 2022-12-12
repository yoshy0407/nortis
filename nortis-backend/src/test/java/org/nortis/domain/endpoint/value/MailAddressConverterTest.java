package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MailAddressConverterTest {

	MailAddressConverter converter = new MailAddressConverter();
	
	@Test
	void testConvertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(MailAddress.create("test@example.com"))).isEqualTo("test@example.com");
	}

	@Test
	void testConvertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute("test@example.com")).isEqualTo(MailAddress.create("test@example.com"));
	}

}
