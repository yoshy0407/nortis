package org.nortis.infrastructure.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class LocalDateTimeConverterTest {

	LocalDateTimeConverter converter = new LocalDateTimeConverter();
	
	@Test
	void testConvertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(LocalDateTime.of(2012,10,3, 3, 4)))
			.isEqualTo(Timestamp.valueOf("2012-10-3 03:04:00"));
	}

	@Test
	void testConvertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute(Timestamp.valueOf("2012-10-3 03:04:00")))
			.isEqualTo(LocalDateTime.of(2012,10,3, 3, 4));
	}

}
