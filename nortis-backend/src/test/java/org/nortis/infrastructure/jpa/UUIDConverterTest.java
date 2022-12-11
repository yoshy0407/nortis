package org.nortis.infrastructure.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UUIDConverterTest {

	UUIDConverter converter = new UUIDConverter();
	
	@Test
	void testConvertToDatabaseColumn() {
		UUID uuid = UUID.randomUUID();
		assertThat(converter.convertToDatabaseColumn(uuid)).isEqualTo(uuid.toString());
	}

	@Test
	void testConvertToEntityAttribute() {
		UUID uuid = UUID.randomUUID();
		assertThat(converter.convertToEntityAttribute(uuid.toString())).isEqualTo(uuid);
	}

}
