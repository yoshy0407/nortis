package org.nortis.domain.authentication.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.exception.DomainException;

class ApiKeyTest {

	@Test
	void testHashCode() throws DomainException {
		ApiKey apiKey1 = ApiKey.create("TESTVALUE");
		ApiKey apiKey2 = ApiKey.create("TESTVALUE");
		
		assertThat(apiKey1.hashCode()).isEqualTo(apiKey2.hashCode());
	}

	@Test
	void testToString() throws DomainException {
		ApiKey apiKey1 = ApiKey.create("TESTVALUE");
		
		assertThat(apiKey1.toString()).isEqualTo("TESTVALUE");
	}

	@Test
	void testNewKey() throws DomainException {
		ApiKey apiKey = ApiKey.newKey();
		
		assertThat(apiKey.toString()).hasSize(36);
	}

	@Test
	void testEqualsObject() throws DomainException {
		ApiKey apiKey1 = ApiKey.create("TESTVALUE");
		ApiKey apiKey2 = ApiKey.create("TESTVALUE");
		
		assertThat(apiKey1.equals(apiKey2)).isTrue();
	}

}
