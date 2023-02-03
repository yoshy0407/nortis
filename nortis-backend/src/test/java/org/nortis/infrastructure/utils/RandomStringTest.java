package org.nortis.infrastructure.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RandomStringTest {

	@Test
	void testDefaultLength() {
		String result = RandomString.defaultLength().build();
		
		assertThat(result).hasSize(10);
	}

	@Test
	void testOf() {
		String result = RandomString.of(15).build();
		
		assertThat(result).hasSize(15);
	}

}
