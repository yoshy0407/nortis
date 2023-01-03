package org.nortis.domain.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class MailConsumerTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}
	
	@Test
	void testUpdateMailAddress() {
		MailConsumer mailConsumer = MailConsumer.create(
				EndpointId.create("TEST_ID"),
				MailAddress.create("hoge@exampple.com"),
				"userId");
		
		mailConsumer.updateMailAddress(MailAddress.create("test@example.com"), "testId");

		assertThat(mailConsumer.getMailAddress())
			.isEqualTo(MailAddress.create("test@example.com"));
		assertThat(mailConsumer.getUpdateId()).isEqualTo("testId");
		assertThat(mailConsumer.getUpdateDt()).isBefore(LocalDateTime.now());
		//Domaによってバージョンが更新されるため1のまま
		assertThat(mailConsumer.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreate() {
		MailConsumer mailConsumer = MailConsumer.create(
				EndpointId.create("TEST_ID"), 
				MailAddress.create("hoge@example.com"), 
				"userId");

		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("TEST_ID"));
		assertThat(mailConsumer.getMailAddress())
			.isEqualTo(MailAddress.create("hoge@example.com"));
		assertThat(mailConsumer.getCreateId()).isEqualTo("userId");
		assertThat(mailConsumer.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(mailConsumer.getUpdateId()).isNull();
		assertThat(mailConsumer.getUpdateDt()).isNull();
		assertThat(mailConsumer.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateEndpointIdNull() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(null, MailAddress.create("hoge@example.com"), "userId");
		}, "エンドポイントIDが未設定です");
	}

	@Test
	void testCreateMailAddressNull() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(EndpointId.create("TEST_ID"), null, "userId");
		}, "メールアドレスが未設定です");
	}

	@Test
	void testCreateUserIdNull() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(
					EndpointId.create("TEST_ID"), 
					MailAddress.create("hoge@example.com"), 
					null);
		}, "作成者IDが未設定です");
	}

	@Test
	void testCreateUserIdEmpty() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(
					EndpointId.create("TEST_ID"), 
					MailAddress.create("hoge@example.com"), 
					"");
		}, "作成者IDが未設定です");
	}

}
