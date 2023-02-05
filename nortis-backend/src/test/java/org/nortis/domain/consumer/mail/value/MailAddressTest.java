package org.nortis.domain.consumer.mail.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.exception.DomainException;

class MailAddressTest {

	@Test
	void testHashCodeEqual() throws DomainException {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test@example.com");
	
		assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
	}

	@Test
	void testHashCodeNotEqual() throws DomainException {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test1@example.com");
	
		assertThat(address1.hashCode()).isNotEqualTo(address2.hashCode());
	}

	@Test
	void testToString() throws DomainException {
		MailAddress address1 = MailAddress.create("test@example.com");
	
		assertThat(address1.toString()).isEqualTo("test@example.com");
	}

	@Test
	void testEquals() throws DomainException {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test@example.com");
	
		assertThat(address1.equals(address2)).isTrue();
	}

	@Test
	void testNotEquals() throws DomainException {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test1@example.com");
	
		assertThat(address1.equals(address2)).isFalse();
	}
	
	@Test
	void testNullError() {
		assertThrows(DomainException.class, () -> {
			MailAddress.create(null);
		}, "メールアドレスが未設定です");
	}

	@Test
	void testEmptyError() {
		assertThrows(DomainException.class, () -> {
			MailAddress.create("");
		}, "メールアドレスが未設定です");
	}

	@Test
	void testContainsError() {
		assertThrows(DomainException.class, () -> {
			MailAddress.create("hoge");
		}, "メールアドレスのフォーマットが不正です");
	}

}
