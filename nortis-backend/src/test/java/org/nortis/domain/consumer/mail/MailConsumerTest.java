package org.nortis.domain.consumer.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.domain.endpoint.value.EndpointId;
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
	void testAddMailAddress() {
		MailConsumer mailConsumer = MailConsumer.create(
				EndpointId.create("TEST_ID"), 
				Lists.list(
						MailAddress.create("hoge@example.com"),
						MailAddress.create("hoge2@example.com")), 
				"userId");
		
		mailConsumer.addMailAddress(MailAddress.create("hoge3@example.com"));
		
		assertThat(mailConsumer.getMailList()).hasSize(3);
		MailInfo info = mailConsumer.getMailList().get(2);
		assertThat(info.getConsumerId()).isEqualTo(mailConsumer.getConsumerId());
		assertThat(info.getOrderNo()).isEqualTo(3);
		assertThat(info.getMailAddress()).isEqualTo(MailAddress.create("hoge3@example.com"));
		assertThat(info.isAdd()).isTrue();
	}

	@Test
	void testRemoveMailAddress() {
		MailConsumer mailConsumer = MailConsumer.create(
				EndpointId.create("TEST_ID"), 
				Lists.list(
						MailAddress.create("hoge@example.com"),
						MailAddress.create("hoge2@example.com")), 
				"userId");
		
		mailConsumer.removeMailAddress(MailAddress.create("hoge2@example.com"));
		
		MailInfo info = mailConsumer.getMailList().get(1);
		assertThat(info.isRemove()).isTrue();
		assertThat(mailConsumer.getMailList().get(0).isRemove()).isFalse();
	}

	@Test
	void testCreate() {
		MailConsumer mailConsumer = MailConsumer.create(
				EndpointId.create("TEST_ID"), 
				Lists.list(
						MailAddress.create("hoge@example.com"),
						MailAddress.create("hoge2@example.com")), 
				"userId");

		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("TEST_ID"));
		
		assertThat(mailConsumer.getMailList()).hasSize(2);
		MailInfo info1 = mailConsumer.getMailList().get(0);
		assertThat(info1.getConsumerId()).isEqualTo(mailConsumer.getConsumerId());
		assertThat(info1.getOrderNo()).isEqualTo(1);
		assertThat(info1.getMailAddress()).isEqualTo(MailAddress.create("hoge@example.com"));

		MailInfo info2 = mailConsumer.getMailList().get(1);
		assertThat(info2.getConsumerId()).isEqualTo(mailConsumer.getConsumerId());
		assertThat(info2.getOrderNo()).isEqualTo(2);
		assertThat(info2.getMailAddress()).isEqualTo(MailAddress.create("hoge2@example.com"));
		
		assertThat(mailConsumer.getCreateId()).isEqualTo("userId");
		assertThat(mailConsumer.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(mailConsumer.getUpdateId()).isNull();
		assertThat(mailConsumer.getUpdateDt()).isNull();
		assertThat(mailConsumer.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateEndpointIdNull() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(null, Lists.list(MailAddress.create("hoge@example.com")), "userId");
		}, "エンドポイントIDが未設定です");
	}

	@Test
	void testCreateMailAddressNull() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(EndpointId.create("TEST_ID"), null, "userId");
		}, "メールアドレスは必ず設定する必要があります");
	}

	@Test
	void testCreateUserIdNull() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(
					EndpointId.create("TEST_ID"), 
					Lists.list(MailAddress.create("hoge@example.com")), 
					null);
		}, "作成者IDが未設定です");
	}

	@Test
	void testCreateUserIdEmpty() {
		assertThrows(DomainException.class, () -> {
			MailConsumer.create(
					EndpointId.create("TEST_ID"), 
					Lists.list(MailAddress.create("hoge@example.com")), 
					"");
		}, "作成者IDが未設定です");
	}

}
