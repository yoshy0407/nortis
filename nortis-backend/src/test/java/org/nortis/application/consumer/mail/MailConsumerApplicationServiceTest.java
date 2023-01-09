package org.nortis.application.consumer.mail;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.nortis.domain.consumer.mail.MailConsumer;
import org.nortis.domain.consumer.mail.MailConsumerRepository;
import org.nortis.domain.consumer.mail.MailInfo;
import org.nortis.domain.consumer.mail.value.ConsumerId;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropEndpoint.sql",
		"/META-INF/ddl/dropMailConsumer.sql",
		"/META-INF/ddl/dropMailInfo.sql",
		"/ddl/createEndpoint.sql",
		"/ddl/createMailConsumer.sql",
		"/ddl/createMailInfo.sql",
		"/META-INF/data/application/del_ins_endpoint.sql",
		"/META-INF/data/application/del_ins_mailConsumer.sql",
		"/META-INF/data/application/del_ins_mailInfo.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = { 
		DomaAutoConfiguration.class, 
		DomaConfiguration.class, 
		MaillConsumerApplicationServiceTestConfig.class
	})
class MailConsumerApplicationServiceTest {

	@Autowired
	MailConsumerApplicationService applicationService;
	
	@Autowired
	MailConsumerRepository mailConsumerRepository;
	
	@Test
	void testRegister() {
		MailRegisterCommand command = new MailRegisterCommand("TEST1", "ENDPOINT1", Lists.list("test1@example.com"), "TEST_ID");
		MailConsumer result = this.applicationService.register(command, data -> {
			return data;
		});
		
		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(result.getConsumerId());
	
		assertThat(optMailConsumer).isPresent();
		MailConsumer mailConsumer = optMailConsumer.get();
		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("ENDPOINT1"));
		
		assertThat(mailConsumer.getMailList()).hasSize(1);
		MailInfo info1 = mailConsumer.getMailList().get(0);
		assertThat(info1.getConsumerId()).isEqualTo(mailConsumer.getConsumerId());
		assertThat(info1.getOrderNo()).isEqualTo(1);
		assertThat(info1.getMailAddress()).isEqualTo(MailAddress.create("test1@example.com"));
	}

	@Test
	void testAddMailAddress() {
		MailAddressAddCommand command = new MailAddressAddCommand(
				"68E75233-7C82-40A6-A34D-CD5FD858EFA6", 
				Lists.list("test2@example.com"),
				"TEST_ID");
		this.applicationService
			.addMailAddress(command, data -> data);

		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(ConsumerId.create("68E75233-7C82-40A6-A34D-CD5FD858EFA6"));
		
		assertThat(optMailConsumer).isPresent();
		MailConsumer mailConsumer = optMailConsumer.get();
		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("TEST1"));

		assertThat(mailConsumer.getMailList()).hasSize(2);
		MailInfo info1 = mailConsumer.getMailList().get(1);
		assertThat(info1.getConsumerId()).isEqualTo(mailConsumer.getConsumerId());
		assertThat(info1.getOrderNo()).isEqualTo(2);
		assertThat(info1.getMailAddress()).isEqualTo(MailAddress.create("test2@example.com"));
	}
	
	@Test
	void testDeleteMailAddress() {
		MailAddressDeleteCommand command = new MailAddressDeleteCommand(
				"7E7BC9C2-133A-49E5-9B34-F52078D6056D", 
				Lists.list("hoge@example.com"), 
				"TEST_ID");
		
		this.applicationService.deleteMailAddress(command, data -> data);
		
		Optional<MailConsumer> optMailConsumer = 
				this.mailConsumerRepository.get(ConsumerId.create("7E7BC9C2-133A-49E5-9B34-F52078D6056D"));

		assertThat(optMailConsumer).isPresent();
		MailConsumer mailConsumer = optMailConsumer.get();
		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("TEST2"));

		assertThat(mailConsumer.getMailList()).hasSize(1);
		MailInfo info1 = mailConsumer.getMailList().get(0);
		assertThat(info1.getConsumerId()).isEqualTo(mailConsumer.getConsumerId());
		assertThat(info1.getOrderNo()).isEqualTo(2);
		assertThat(info1.getMailAddress()).isEqualTo(MailAddress.create("hoge2@example.com"));
	}

	@Test
	void testDelete() {
		this.applicationService
			.delete("3AC62B43-D866-416C-B205-7EC601104F4A");
		
		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(ConsumerId.create("3AC62B43-D866-416C-B205-7EC601104F4A"));
		assertThat(optMailConsumer).isEmpty();
		
	}
	
	@Test
	void testRemoveEndpointIdByEndpointDeleted() {
		this.applicationService
			.removeEndpointIdByEndpointDeleted("TEST1", "TEST_ID");

		Optional<MailConsumer> optMailConsumer1 = 
				this.mailConsumerRepository.get(ConsumerId.create("68E75233-7C82-40A6-A34D-CD5FD858EFA6"));
		assertThat(optMailConsumer1.get().getEndpointId()).isNull();
		Optional<MailConsumer> optMailConsumer2 = 
				this.mailConsumerRepository.get(ConsumerId.create("3AC62B43-D866-416C-B205-7EC601104F4A"));
		assertThat(optMailConsumer2.get().getEndpointId()).isNull();
	}

}
