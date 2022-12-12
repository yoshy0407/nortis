package org.nortis.domain.tenant.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class TenantIdTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}
	
	@Test
	void testHashCodeEqual() {
		TenantId id1 = TenantId.create("TEST1");
		TenantId id2 = TenantId.create("TEST1");
		
		assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
	}

	@Test
	void testHashCodeNotEqual() {
		TenantId id1 = TenantId.create("TEST1");
		TenantId id2 = TenantId.create("TEST2");
		
		assertThat(id1.hashCode()).isNotEqualTo(id2.hashCode());
	}

	@Test
	void testToString() {
		TenantId id1 = TenantId.create("TEST1");
	
		assertThat(id1.toString()).isEqualTo("TEST1");
	}

	@Test
	void testEquals() {
		TenantId id1 = TenantId.create("TEST1");
		TenantId id2 = TenantId.create("TEST1");
		
		assertThat(id1.equals(id2)).isTrue();
	}

	@Test
	void testNotEquals() {
		TenantId id1 = TenantId.create("TEST1");
		TenantId id2 = TenantId.create("TEST2");
		
		assertThat(id1.equals(id2)).isFalse();
	}

	@Test
	void testEmptyError() {
		assertThrows(DomainException.class, () -> {
			TenantId.create("");
		}, "テナントIDが未設定です");
		assertThrows(DomainException.class, () -> {
			TenantId.create(null);
		}, "テナントIDが未設定です");
	}

	@Test
	void testLengthError() {
		assertThrows(DomainException.class, () -> {
			TenantId.create("123456788901");
		}, "テナントIDは10文字以内である必要があります");
	}

}
