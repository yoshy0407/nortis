package org.nortis.domain.user.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.NortisBaseTestConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
		MessageSourceAutoConfiguration.class,
		NortisBaseTestConfiguration.class
})
class UserIdTest {

	@Test
	void testHashCodeTrue() throws DomainException {
		UserId userId1 = UserId.create("0000000001");
		UserId userId2 = UserId.create("0000000001");
		
		assertThat(userId1.hashCode()).isEqualTo(userId2.hashCode());
	}

	@Test
	void testHashCodeFalse() throws DomainException {
		UserId userId1 = UserId.create("0000000001");
		UserId userId2 = UserId.create("0000000010");
		
		assertThat(userId1.hashCode()).isNotEqualTo(userId2.hashCode());
	}

	@Test
	void testToString() throws DomainException {
		assertThat(UserId.create("0000000001").toString()).isEqualTo("0000000001");
	}

	@Test
	void testCreateNullError() {
		assertThrows(DomainException.class, () -> {
			UserId.create(null);
		}, "ユーザIDが未設定です");
	}

	@Test
	void testCreateEmptyError() {
		assertThrows(DomainException.class, () -> {
			UserId.create("");
		}, "ユーザIDが未設定です");
	}

	@Test
	void testCreateLengthError() {
		assertThrows(DomainException.class, () -> {
			UserId.create("12345678901");
		}, "ユーザIDは10文字以内である必要があります");
	}

	@Test
	void testEqualsTrue() throws DomainException {
		UserId userId1 = UserId.create("0000000001");
		UserId userId2 = UserId.create("0000000001");
		
		assertThat(userId1.equals(userId2)).isTrue();
	}

	@Test
	void testEqualsFalse() throws DomainException {
		UserId userId1 = UserId.create("0000000001");
		UserId userId2 = UserId.create("0000000010");
		
		assertThat(userId1.equals(userId2)).isFalse();
	}

}
