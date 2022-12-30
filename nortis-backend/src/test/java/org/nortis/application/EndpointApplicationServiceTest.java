package org.nortis.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
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
	EndpointApplicationService endpointApplicationService;
	
	@Autowired
	EndpointRepository endpointRepository;
	
	@Test
	void testRegisterEndpoint() {
		Endpoint endpoint = endpointApplicationService.registerEndpoint("TEST1", "test-point", "エンドポイント", "TEST_ID", data -> {
			return data;
		});
		
		assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("TEST1"));
		assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("test-point"));
		assertThat(endpoint.getEndpointName()).isEqualTo("エンドポイント");
		
		Optional<Endpoint> opt = this.endpointRepository.get(TenantId.create("TEST1"), EndpointId.create("test-point"));
		
		assertThat(opt).isPresent();
	}

	@Test
	void testChangeName() {
		endpointApplicationService.changeName("TEST2", "ENDPOINT2", "TEST POINT", "TEST_ID", data -> {
			return data;
		});

		Optional<Endpoint> opt = this.endpointRepository.get(TenantId.create("TEST2"), EndpointId.create("ENDPOINT2"));
		
		assertThat(opt).isPresent();
		assertThat(opt.get().getEndpointName()).isEqualTo("TEST POINT");
	}
	
	@Test
	void testDelete() {
		endpointApplicationService.delete("TEST1", "ENDPOINT1");

		Optional<Endpoint> opt = this.endpointRepository.get(TenantId.create("TEST1"), EndpointId.create("ENDPOINT1"));
		
		assertThat(opt).isEmpty();
	}

}
