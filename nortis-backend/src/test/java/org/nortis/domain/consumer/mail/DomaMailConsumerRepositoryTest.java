package org.nortis.domain.consumer.mail;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.value.ConsumerId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.seasar.doma.jdbc.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropMailConsumer.sql",
		"/META-INF/ddl/dropMailInfo.sql",
		"/ddl/createMailConsumer.sql",
		"/ddl/createMailInfo.sql",
		"/META-INF/data/domain/consumer/mail/del_ins_mailConsumer.sql",
		"/META-INF/data/domain/consumer/mail/del_ins_mailInfo.sql"
})
@AutoConfigureDataJdbc
@SpringBootTest(classes = {
		DomaAutoConfiguration.class, 
		DomaConfiguration.class, 
})
class DomaMailConsumerRepositoryTest {

	@Autowired
	Config config;
	
	DomaMailConsumerRepository repository;
	
	@BeforeEach
	void setup() {
		this.repository = new DomaMailConsumerRepository(config);
	}
	
	@Test
	void testGet() {
		Optional<MailConsumer> mailConsumer = 
				this.repository.get(ConsumerId.create("68E75233-7C82-40A6-A34D-CD5FD858EFA6"));
		
		assertThat(mailConsumer).isPresent();
		mailConsumer.ifPresent(consumer -> {
			assertThat(consumer.getConsumerId()).isEqualTo(ConsumerId.create("68E75233-7C82-40A6-A34D-CD5FD858EFA6"));
			assertThat(consumer.getEndpointId()).isEqualTo(EndpointId.create("TEST1"));
			assertThat(consumer.getCreateDt()).isEqualTo(LocalDateTime.of(2022, 1, 5, 12, 56, 34));
			assertThat(consumer.getCreateId()).isEqualTo("TEST_ID");
			assertThat(consumer.getUpdateDt()).isNull();
			assertThat(consumer.getUpdateId()).isNull();
			assertThat(consumer.getVersion()).isEqualTo(1L);
			
			assertThat(consumer.getMailList()).hasSize(1);
			MailInfo info = consumer.getMailList().get(0);
			assertThat(info.getConsumerId()).isNotNull();
			assertThat(info.getOrderNo()).isEqualTo(1);
			assertThat(info.getMailAddress()).isEqualTo(MailAddress.create("hoge@example.com"));
		});
	}

	@Test
	void testGetFromEndpoint() {
		List<MailConsumer> consumerList = this.repository.getFromEndpoint(EndpointId.create("TEST1"));
		
		assertThat(consumerList).hasSize(2);
		assertThat(consumerList.get(0).getMailList()).hasSize(1);
		assertThat(consumerList.get(1).getMailList()).hasSize(2);
	}

	@Test
	void testSave() {
		MailConsumer mailConsumer = MailConsumer.create(
				EndpointId.create("EPOINT1"), 
				Lists.list(MailAddress.create("hoge@example.com")), 
				"TEST_ID");
		
		this.repository.save(mailConsumer);
		
		Optional<MailConsumer> optConsumer = 
				this.repository.get(mailConsumer.getConsumerId());
		
		assertThat(optConsumer).isPresent();
		optConsumer.ifPresent(consumer -> {
			assertThat(consumer.getConsumerId()).isNotNull();
			assertThat(consumer.getEndpointId()).isEqualTo(EndpointId.create("EPOINT1"));
			assertThat(consumer.getCreateDt()).isBefore(LocalDateTime.now());
			assertThat(consumer.getCreateId()).isEqualTo("TEST_ID");
			assertThat(consumer.getUpdateDt()).isNull();
			assertThat(consumer.getUpdateId()).isNull();
			assertThat(consumer.getVersion()).isEqualTo(1L);
			
			assertThat(consumer.getMailList()).hasSize(1);
			MailInfo info = consumer.getMailList().get(0);
			assertThat(info.getConsumerId()).isNotNull();
			assertThat(info.getOrderNo()).isEqualTo(1);
			assertThat(info.getMailAddress()).isEqualTo(MailAddress.create("hoge@example.com"));
		});

	}

	@Test
	void testUpdate() {
		Optional<MailConsumer> mailConsumer = 
				this.repository.get(ConsumerId.create("7E7BC9C2-133A-49E5-9B34-F52078D6056D"));
		
		assertThat(mailConsumer).isPresent();
		mailConsumer.ifPresent(consumer -> {
			consumer.addMailAddress(MailAddress.create("example@example.com"));
			consumer.removeMailAddress(MailAddress.create("hoge2@example.com"));
			this.repository.update(consumer);
		});	
		
		Optional<MailConsumer> mailConsumer2 = 
				this.repository.get(ConsumerId.create("7E7BC9C2-133A-49E5-9B34-F52078D6056D"));
		
		assertThat(mailConsumer2).isPresent();
		mailConsumer2.ifPresent(consumer -> {
			
			assertThat(consumer.getMailList()).hasSize(2);
			MailInfo info1 = consumer.getMailList().get(0);
			assertThat(info1.getConsumerId()).isNotNull();
			assertThat(info1.getOrderNo()).isEqualTo(1);
			assertThat(info1.getMailAddress()).isEqualTo(MailAddress.create("hoge@example.com"));

			MailInfo info2 = consumer.getMailList().get(1);
			assertThat(info2.getConsumerId()).isNotNull();
			assertThat(info2.getOrderNo()).isEqualTo(3);
			assertThat(info2.getMailAddress()).isEqualTo(MailAddress.create("example@example.com"));
		});

	}

	@Test
	void testRemove() {
		Optional<MailConsumer> mailConsumer = 
				this.repository.get(ConsumerId.create("7E7BC9C2-133A-49E5-9B34-F52078D6056D"));
		
		this.repository.remove(mailConsumer.get());

		Optional<MailConsumer> mailConsumer2 = 
				this.repository.get(ConsumerId.create("7E7BC9C2-133A-49E5-9B34-F52078D6056D"));
		
		assertThat(mailConsumer2).isEmpty();
	}

}
