package org.nortis.application.tenant;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropTenant.sql",
		"/META-INF/ddl/dropAuthentication.sql",
		"/ddl/createTenant.sql",
		"/ddl/createAuthentication.sql",
		"/META-INF/data/application/del_ins_tenant.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = { DomaAutoConfiguration.class, DomaConfiguration.class, TenantApplicationServiceTestConfig.class })
class TenantApplicationServiceTest {

	@Autowired
	ApplicationEvents applicationEvents;
	
	@Autowired
	TenantApplicationService tenantApplicationService;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	AuthenticationRepository authenticationRepository;
	
	@Test
	void testRegister() {
		TenantRegisterCommand command = new TenantRegisterCommand("TENANT1", "登録テナント", "TEST_ID");
		TenantId tenantId = tenantApplicationService
				.register(command, data -> data.getTenantId());
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		assertThat(optTenant).isPresent();
		
		Tenant tenant = optTenant.get();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TENANT1"));
		assertThat(tenant.getTenantName()).isEqualTo("登録テナント");
		assertThat(tenant.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNull();
		assertThat(tenant.getUpdateId()).isNull();
		assertThat(tenant.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateApiKey() {
		ApiKey apiKey = tenantApplicationService.createApiKey("TEST2");
		
		Optional<Authentication> optAuth = authenticationRepository.get(apiKey);
		assertThat(optAuth).isPresent();
		
		Authentication auth = optAuth.get();
		assertThat(auth.getApiKey()).isNotNull();
		assertThat(auth.getTenantId()).isEqualTo(TenantId.create("TEST2"));
		assertThat(auth.getUserId()).isNull();
		
	}
	
	@Test
	void testChangeName() {
		TenantNameUpdateCommand command = new TenantNameUpdateCommand("TEST2", "テストテナント", "USER_ID");
		TenantId tenantId = tenantApplicationService
				.changeName(command, data -> data.getTenantId());

		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		assertThat(optTenant).isPresent();
		
		Tenant tenant = optTenant.get();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TEST2"));
		assertThat(tenant.getTenantName()).isEqualTo("テストテナント");
		assertThat(tenant.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isBefore(LocalDateTime.now());
		assertThat(tenant.getUpdateId()).isEqualTo("USER_ID");
		assertThat(tenant.getVersion()).isEqualTo(2L);
	}
		
	@Test
	void testDelete() {
		tenantApplicationService.delete("TEST3", "USER_ID");

		Optional<Tenant> optTenant = this.tenantRepository.get(TenantId.create("TEST3"));
		assertThat(optTenant).isEmpty();	
		
		this.applicationEvents.stream(TenantDeletedEvent.class).forEach(event -> {
			assertThat(event.getTenantId()).isEqualTo(TenantId.create("TEST3"));
			assertThat(event.getUpdateUserId()).isEqualTo("USER_ID");
		});
	}

}
