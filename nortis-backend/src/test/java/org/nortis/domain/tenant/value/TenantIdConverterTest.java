package org.nortis.domain.tenant.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TenantIdConverterTest {

	TenantIdConverter converter = new TenantIdConverter();
	
	@Test
	void testConvertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(TenantId.of("TEST"))).isEqualTo("TEST");
	}

	@Test
	void testConvertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute("TEST")).isEqualTo(TenantId.of("TEST"));
	}

}
