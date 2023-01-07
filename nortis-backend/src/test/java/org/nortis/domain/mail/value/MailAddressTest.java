package org.nortis.domain.mail.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class MailAddressTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}
	
	@Test
	void testHashCodeEqual() {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test@example.com");
	
		assertThat(address1.hashCode()).isEqualTo(address2.hashCode());
	}

	@Test
	void testHashCodeNotEqual() {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test1@example.com");
	
		assertThat(address1.hashCode()).isNotEqualTo(address2.hashCode());
	}

	@Test
	void testToString() {
		MailAddress address1 = MailAddress.create("test@example.com");
	
		assertThat(address1.toString()).isEqualTo("test@example.com");
	}

	@Test
	void testEquals() {
		MailAddress address1 = MailAddress.create("test@example.com");
		MailAddress address2 = MailAddress.create("test@example.com");
	
		assertThat(address1.equals(address2)).isTrue();
	}

	@Test
	void testNotEquals() {
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
