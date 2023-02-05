package org.nortis.application.event;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.function.Supplier;
import org.dbunit.DatabaseUnitException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.nortis.domain.consumer.ConsumeFailureException;
import org.nortis.domain.consumer.Message;
import org.nortis.domain.consumer.MessageConsumer;
import org.nortis.domain.event.value.EventId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.DbUnitConfiguration;
import org.nortis.test.DbUnitTableAssert;
import org.nortis.test.NortisBaseTestConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropReceiveEvent.sql",
		"/META-INF/ddl/dropTenant.sql",
		"/META-INF/ddl/dropEndpoint.sql",
		"/ddl/createReceiveEvent.sql",
		"/ddl/createTenant.sql",
		"/ddl/createEndpoint.sql",
		"/META-INF/data/application/del_ins_receiveEvent.sql",
		"/META-INF/data/application/del_ins_tenant.sql",
		"/META-INF/data/application/del_ins_endpoint.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = { 
		DomaAutoConfiguration.class, 
		DomaConfiguration.class, 
		NortisBaseTestConfiguration.class,
		ReceiveEventApplicationServiceTestConfig.class,
		DbUnitConfiguration.class
})
class ReceiveEventApplicationServiceTest {

	@MockBean
	MessageConsumer messageConsumer;

	@Autowired
	ReceiveEventApplicationService receiveEventApplicationService;

	@Autowired
	Supplier<DbUnitTableAssert> assertFactory;
	
	@Test
	void testRegister() throws DatabaseUnitException, DomainException {
		ReceiveEventRegisterCommand command = new ReceiveEventRegisterCommand(
				"TEST1", 
				"ENDPOINT1", 
				"{ \"name\": \"John\", \"age\": \"34\" }");
		
		EventId eventId = this.receiveEventApplicationService.register(command, data -> data.getEventId());
		
		String sql = """
					SELECT
						TENANT_ID,
						ENDPOINT_ID,
						SUBSCRIBED,
						TEMPLATE_PARAMETER
					FROM
						RECEIVE_EVENT
					WHERE
						EVENT_ID = '%s'				
				""";
		
		this.assertFactory.get()
			.expect("classpath:/META-INF/assert/application/testRegister.xml", "RECEIVE_EVENT")
			.actual("RECEIVE_EVENT", sql.formatted(eventId.toString()))
			.assertThat();
	}

	@Test
	void testSubscribeEvent() throws ConsumeFailureException {
		this.receiveEventApplicationService.subscribeEvent();
		
		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
		Mockito.verify(this.messageConsumer, times(4)).consume(captor.capture());
		
		List<Message> messages = captor.getAllValues();
		assertThat(messages.get(0).getSubject()).isEqualTo("subject");
		assertThat(messages.get(0).getMessageBody()).isEqualTo("message");
		assertThat(messages.get(1).getSubject()).isEqualTo("subject");
		assertThat(messages.get(1).getMessageBody()).isEqualTo("message");
		assertThat(messages.get(2).getSubject()).isEqualTo("subject");
		assertThat(messages.get(2).getMessageBody()).isEqualTo("message");
		assertThat(messages.get(3).getSubject()).isEqualTo("subject");
		assertThat(messages.get(3).getMessageBody()).isEqualTo("message");
	}

	@Test
	void testSubscribedByEndpointDeleted() throws DatabaseUnitException, DomainException {		
		this.receiveEventApplicationService.subscribedByEndpointDeleted("ENDPOINT3");

		this.assertFactory.get()
			.expect("classpath:/META-INF/assert/application/testSubscribedByEndpointDeleted.xml", "RECEIVE_EVENT")
			.actual("RECEIVE_EVENT", """
					SELECT
						EVENT_ID,
						TENANT_ID,
						ENDPOINT_ID,
						SUBSCRIBED,
						TEMPLATE_PARAMETER
					FROM
						RECEIVE_EVENT
					WHERE
						ENDPOINT_ID = 'ENDPOINT3'
					""")
			.assertThat();
	}

}
