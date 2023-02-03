package org.nortis.domain.endpoint.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.NortisBaseTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		NortisBaseTestConfiguration.class
})
class EndpointIdTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}
	
	@Test
	void testHashCodeEqual() {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT");
		
		assertThat(endpointId1.hashCode()).isEqualTo(endpointId2.hashCode());
	}

	@Test
	void testHashCodeNotEqual() {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT1");
		
		assertThat(endpointId1.hashCode()).isNotEqualTo(endpointId2.hashCode());
	}

	@Test
	void testToString() {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		
		assertThat(endpointId1.toString()).isEqualTo("ENDPOINT");
	}

	@Test
	void testEquals() {
		EndpointId endpointId1 = EndpointId.create("ENDPOINT");
		EndpointId endpointId2 = EndpointId.create("ENDPOINT");
		
		assertThat(endpointId1.equals(endpointId2)).isTrue();
	}

	@Test
	void testNotEquals() {
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
