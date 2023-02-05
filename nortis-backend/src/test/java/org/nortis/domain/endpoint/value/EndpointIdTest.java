package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.exception.DomainException;

class EndpointIdTest {

	@Test
	void testHashCodeEqual() throws DomainException {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT");
		
		assertThat(endpointId1.hashCode()).isEqualTo(endpointId2.hashCode());
	}

	@Test
	void testHashCodeNotEqual() throws DomainException {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT1");
		
		assertThat(endpointId1.hashCode()).isNotEqualTo(endpointId2.hashCode());
	}

	@Test
	void testToString() throws DomainException {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		
		assertThat(endpointId1.toString()).isEqualTo("ENDPOINT");
	}

	@Test
	void testEquals() throws DomainException {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT");
		
		assertThat(endpointId1.equals(endpointId2)).isTrue();
	}

	@Test
	void testNotEquals() throws DomainException {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT1");
		
		assertThat(endpointId1.equals(endpointId2)).isFalse();
	}

	@Test
	void testEmptyError() {
		assertThrows(DomainException.class, () -> {
			EndpointId.create("");
		}, "エンドポイントIDが未設定です");
	}

	@Test
	void testNullError() {
		assertThrows(DomainException.class, () -> {
			EndpointId.create(null);
		}, "エンドポイントIDが未設定です");
	}

}
