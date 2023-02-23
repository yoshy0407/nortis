package org.nortis.application.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUser;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropTenant.sql",
		"/META-INF/ddl/dropSuser.sql",
		"/META-INF/ddl/dropAuthentication.sql",
		"/ddl/createTenant.sql",
		"/ddl/createSuser.sql",
		"/ddl/createAuthentication.sql",
		"/META-INF/data/application/del_ins_tenant.sql",
		"/META-INF/data/application/del_ins_suser.sql"
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
	void testGet() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);
		
		Tenant tenant = this.tenantApplicationService.getTenant("TEST1", user, d -> d);

		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TEST1"));
		assertThat(tenant.getTenantName()).isEqualTo("テストテナント１");
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getUpdateDt()).isNull();
		assertThat(tenant.getUpdateId()).isNull();
		assertThat(tenant.getVersion()).isEqualTo(1L);
	}

	@Test
	void testGetTenantNotFound() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			this.tenantApplicationService.getTenant("AAAAAA", user, d -> d);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
		assertThat(ex.getMessage()).isEqualTo("指定されたIDのテナントは存在しません");
	}

	@Test
	void testGetTenantAccessDenied() throws DomainException {
		NortisUser user = new NortisUser("0000000002", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			this.tenantApplicationService.getTenant("TEST1", user, d -> d);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
	}

	@Test
	void testRegister() throws DomainException {
		NortisUser user = new NortisUser("0000000005", new String[] {}, "password", false, Lists.newArrayList(), false);

		TenantRegisterCommand command = new TenantRegisterCommand("TENANT1", "登録テナント", user);
		TenantId tenantId = tenantApplicationService
				.register(command, data -> data.getTenantId());

		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		assertThat(optTenant).isPresent();

		Tenant tenant = optTenant.get();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TENANT1"));
		assertThat(tenant.getTenantName()).isEqualTo("登録テナント");
		assertThat(tenant.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(tenant.getCreateId()).isEqualTo("0000000005");
		assertThat(tenant.getUpdateDt()).isNull();
		assertThat(tenant.getUpdateId()).isNull();
		assertThat(tenant.getVersion()).isEqualTo(1L);
	}

	@Test
	void testRegisterAccessDenied() throws DomainException {
		NortisUser user = new NortisUser("0000000003", new String[] {}, "password", false, Lists.newArrayList(), false);

		TenantRegisterCommand command = new TenantRegisterCommand("TENANT1", "登録テナント", user);
		
		DomainException ex = assertThrows(DomainException.class, () -> {
			tenantApplicationService
					.register(command, data -> data.getTenantId());			
		});

		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
	}

	@Test
	void testCreateApiKey() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		ApiKey apiKey = tenantApplicationService.createApiKey("TEST2", user);

		Optional<Authentication> optAuth = authenticationRepository.get(apiKey);
		assertThat(optAuth).isPresent();

		Authentication auth = optAuth.get();
		assertThat(auth.getApiKey()).isNotNull();
		assertThat(auth.getTenantId()).isEqualTo(TenantId.create("TEST2"));
		assertThat(auth.getUserId()).isNull();
	}

	@Test
	void testCreateApiKeyAccessDenied() throws DomainException {
		NortisUser user = new NortisUser("0000000004", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			tenantApplicationService.createApiKey("TEST2", user);			
		});
				
		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
	}
	
	@Test
	void testCreateApiKeyNotFound() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			tenantApplicationService.createApiKey("AAAAAA", user);
		});

		assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
		assertThat(ex.getMessage()).isEqualTo("指定されたIDのテナントは存在しません");
	}

	@Test
	void testChangeName() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		TenantNameUpdateCommand command = new TenantNameUpdateCommand("TEST2", "テストテナント", user);
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
		assertThat(tenant.getUpdateId()).isEqualTo("0000000001");
		assertThat(tenant.getVersion()).isEqualTo(2L);
	}
	
	@Test
	void testChangeNameAccessDenied() throws DomainException {
		NortisUser user = new NortisUser("0000000002", new String[] {}, "password", false, Lists.newArrayList(), false);

		TenantNameUpdateCommand command = new TenantNameUpdateCommand("TEST2", "テストテナント", user);

		DomainException ex = assertThrows(DomainException.class, () -> {
			tenantApplicationService
					.changeName(command, data -> data.getTenantId());			
		});

		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
	}
	
	@Test
	void testChangeNameNotFound() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			TenantNameUpdateCommand command = new TenantNameUpdateCommand("AAAAA", "テストテナント", user);
			tenantApplicationService
					.changeName(command, data -> data.getTenantId());
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
		assertThat(ex.getMessage()).isEqualTo("指定されたIDのテナントは存在しません");
	}

	@Test
	void testDelete() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		tenantApplicationService.delete("TEST3", user);

		Optional<Tenant> optTenant = this.tenantRepository.get(TenantId.create("TEST3"));
		assertThat(optTenant).isEmpty();	

		this.applicationEvents.stream(TenantDeletedEvent.class).forEach(event -> {
			assertThat(event.getTenantId().toString()).isEqualTo("TEST3");
			assertThat(event.getUpdateUserId()).isEqualTo("0000000001");
		});
	}

	@Test
	void testDeleteAccessDenied() throws DomainException {
		NortisUser user = new NortisUser("0000000002", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			tenantApplicationService.delete("TEST3", user);			
		});

		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");

	}

	@Test
	void testDeleteNotFound() throws DomainException {
		NortisUser user = new NortisUser("0000000001", new String[] {}, "password", false, Lists.newArrayList(), false);

		DomainException ex = assertThrows(DomainException.class, () -> {
			tenantApplicationService.delete("AAAA", user);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS10003");
		assertThat(ex.getMessage()).isEqualTo("指定されたIDのテナントは存在しません");
	}

}
