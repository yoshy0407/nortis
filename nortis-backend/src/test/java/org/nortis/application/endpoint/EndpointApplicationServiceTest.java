package org.nortis.application.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropTenant.sql",
		"/META-INF/ddl/dropEndpoint.sql",
		"/ddl/createTenant.sql",
		"/ddl/createEndpoint.sql",
		"/META-INF/data/application/del_ins_tenant.sql",
		"/META-INF/data/application/del_ins_endpoint.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = { 
		DomaAutoConfiguration.class, 
		DomaConfiguration.class, 
		EndpointApplicationServiceTestConfig.class
	})
class EndpointApplicationServiceTest {

	@Autowired
	ApplicationEvents applicationEvents;
	
	@Autowired
	EndpointApplicationService endpointApplicationService;
	
	@Autowired
	EndpointRepository endpointRepository;
	
	@Test
	void testRegisterEndpoint() throws DomainException {
		EndpointRegisterCommand command = 
				new EndpointRegisterCommand("TEST1", "test-point", "エンドポイント", "${name}!!", "Hello! ${name}", "TEST_ID");
		Endpoint endpoint = endpointApplicationService.registerEndpoint(command, data -> {
			return data;
		});
		
		assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("TEST1"));
		assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("test-point"));
		assertThat(endpoint.getEndpointName()).isEqualTo("エンドポイント");
		
		Optional<Endpoint> opt = this.endpointRepository.get(TenantId.create("TEST1"), EndpointId.create("test-point"));
		
		assertThat(opt).isPresent();
	}

	@Test
	void testUpdate() throws DomainException {
		EndpointUpdateCommand command = 
				new EndpointUpdateCommand("TEST2", "ENDPOINT2", "TEST POINT", "件名", "本文", "TEST_ID");
		endpointApplicationService.updateEndpoint(command, data -> {
			return data;
		});

		Optional<Endpoint> opt = this.endpointRepository.get(TenantId.create("TEST2"), EndpointId.create("ENDPOINT2"));
		
		assertThat(opt).isPresent();
		assertThat(opt.get().getEndpointName()).isEqualTo("TEST POINT");
	}
	
	@Test
	void testDelete() throws DomainException {
		EndpointDeleteCommand command = new EndpointDeleteCommand("TEST1", "ENDPOINT1", "TEST_ID");
		endpointApplicationService.delete(command);

		Optional<Endpoint> opt = this.endpointRepository.get(TenantId.create("TEST1"), EndpointId.create("ENDPOINT1"));
		
		assertThat(opt).isEmpty();
		
		applicationEvents.stream(EndpointDeletedEvent.class)
			.forEach(event -> {
				assertThat(event.getTenantId().toString()).isEqualTo("TEST1");
				assertThat(event.getEndpointId().toString()).isEqualTo("ENDPOINT1");
				assertThat(event.getUpdateUserId()).isEqualTo("TEST_ID");
			});
	}
	
	@Test
	void testDeleteFromTenantId() throws DomainException {
		endpointApplicationService.deleteFromTenantId("TEST3", "USER_ID");

		Optional<Endpoint> opt1 = this.endpointRepository.get(TenantId.create("TEST3"), EndpointId.create("ENDPOINT3"));		
		assertThat(opt1).isEmpty();
		Optional<Endpoint> opt2 = this.endpointRepository.get(TenantId.create("TEST3"), EndpointId.create("ENDPOINT4"));		
		assertThat(opt2).isEmpty();
		
		applicationEvents.stream(EndpointDeletedEvent.class)
			.forEach(event -> {
				assertThat(event.getTenantId().toString()).isEqualTo("TEST3");
				assertThat(event.getEndpointId()).isNotNull();
				assertThat(event.getUpdateUserId()).isEqualTo("USER_ID");
			});
}

}
