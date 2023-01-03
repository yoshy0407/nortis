package org.nortis.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.nortis.application.mail.MailAddressChangeCommand;
import org.nortis.application.mail.MailConsumerApplicationService;
import org.nortis.application.mail.MailRegisterCommand;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.MailConsumer;
import org.nortis.domain.mail.MailConsumerRepository;
import org.nortis.domain.mail.value.ConsumerId;
import org.nortis.domain.mail.value.MailAddress;
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
		"/ddl/createEndpoint.sql",
		"/ddl/createMailConsumer.sql",
		"/META-INF/data/application/del_ins_endpoint.sql",
		"/META-INF/data/application/del_ins_mailConsumer.sql"
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
		MailRegisterCommand command = new MailRegisterCommand("TEST1", "ENDPOINT1", "test1@example.com", "TEST_ID");
		MailConsumer result = this.applicationService.register(command, data -> {
			return data;
		});
		
		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(result.getConsumerId());
	
		assertThat(optMailConsumer).isPresent();
		MailConsumer mailConsumer = optMailConsumer.get();
		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("ENDPOINT1"));
		assertThat(mailConsumer.getMailAddress()).isEqualTo(MailAddress.create("test1@example.com"));
	}

	@Test
	void testChangeMailAddress() {
		MailAddressChangeCommand command = new MailAddressChangeCommand("68E75233-7C82-40A6-A34D-CD5FD858EFA6", "test2@example.com", "TEST_ID");
		this.applicationService
			.changeMailAddress(command, data -> data);

		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(ConsumerId.create("68E75233-7C82-40A6-A34D-CD5FD858EFA6"));
		
		assertThat(optMailConsumer).isPresent();
		MailConsumer mailConsumer = optMailConsumer.get();
		assertThat(mailConsumer.getConsumerId()).isNotNull();
		assertThat(mailConsumer.getEndpointId()).isEqualTo(EndpointId.create("TEST1"));
		assertThat(mailConsumer.getMailAddress()).isEqualTo(MailAddress.create("test2@example.com"));
}

	@Test
	void testDelete() {
		this.applicationService
			.delete("3AC62B43-D866-416C-B205-7EC601104F4A");
		
		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(ConsumerId.create("3AC62B43-D866-416C-B205-7EC601104F4A"));
		assertThat(optMailConsumer).isEmpty();
		
	}

}
