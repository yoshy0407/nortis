package org.nortis.domain.authentication.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiKeyTest {

	@Test
	void testHashCode() {
		ApiKey apiKey1 = ApiKey.create("TESTVALUE");
		ApiKey apiKey2 = ApiKey.create("TESTVALUE");
		
		assertThat(apiKey1.hashCode()).isEqualTo(apiKey2.hashCode());
	}

	@Test
	void testToString() {
		ApiKey apiKey1 = ApiKey.create("TESTVALUE");
		
		assertThat(apiKey1.toString()).isEqualTo("TESTVALUE");
	}

	@Test
	void testNewKey() {
		ApiKey apiKey = ApiKey.newKey();
		
		assertThat(apiKey.toString()).hasSize(36);
	}

	@Test
	void testEqualsObject() {
		ApiKey apiKey1 = ApiKey.create("TESTVALUE");
		ApiKey apiKey2 = ApiKey.create("TESTVALUE");
		
		assertThat(apiKey1.equals(apiKey2)).isTrue();
	}

}
