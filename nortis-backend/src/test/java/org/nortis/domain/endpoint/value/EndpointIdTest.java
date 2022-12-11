package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class EndpointIdTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}
	
	@Test
	void testHashCodeEqual() {
		EndpointId endpointId1 = EndpointId.of("ENDPOINT");
		EndpointId endpointId2 = EndpointId.of("ENDPOINT");
		
		assertThat(endpointId1.hashCode()).isEqualTo(endpointId2.hashCode());
	}

	@Test
	void testHashCodeNotEqual() {
		EndpointId endpointId1 = EndpointId.of("ENDPOINT");
		EndpointId endpointId2 = EndpointId.of("ENDPOINT1");
		
		assertThat(endpointId1.hashCode()).isNotEqualTo(endpointId2.hashCode());
	}

	@Test
	void testToString() {
		EndpointId endpointId1 = EndpointId.of("ENDPOINT");
		
		assertThat(endpointId1.toString()).isEqualTo("ENDPOINT");
	}

	@Test
	void testEquals() {
		EndpointId endpointId1 = EndpointId.of("ENDPOINT");
		EndpointId endpointId2 = EndpointId.of("ENDPOINT");
		
		assertThat(endpointId1.equals(endpointId2)).isTrue();
	}

	@Test
	void testNotEquals() {
		EndpointId endpointId1 = EndpointId.of("ENDPOINT");
		EndpointId endpointId2 = EndpointId.of("ENDPOINT1");
		
		assertThat(endpointId1.equals(endpointId2)).isFalse();
	}

	@Test
	void testEmptyError() {
		assertThrows(DomainException.class, () -> {
			EndpointId.of("");
		}, "エンドポイントIDが未設定です");
	}

	@Test
	void testNullError() {
		assertThrows(DomainException.class, () -> {
			EndpointId.of(null);
		}, "エンドポイントIDが未設定です");
	}

}
