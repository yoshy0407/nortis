package org.nortis.application.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropAuthentication.sql",
		"/META-INF/ddl/dropSuser.sql",
		"/META-INF/ddl/dropTenant.sql",
		"/ddl/createAuthentication.sql",
		"/ddl/createSuser.sql",
		"/ddl/createTenant.sql",
		"/META-INF/data/application/del_ins_authentication.sql",
		"/META-INF/data/application/del_ins_suser.sql",
		"/META-INF/data/application/del_ins_tenant.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = {
		DomaAutoConfiguration.class, 
		DomaConfiguration.class, 
		SuserApplicationServiceTestConfig.class
})
class SuserApplicationServiceTest {

	@Autowired
	SuserApplicationService suserApplicationService;

	@Autowired
	SuserRepository suserRepository;

	@Test
	void testRegisterAdminUser() throws DomainException {
		SuserRegisterCommand command = 
				new SuserRegisterCommand("1000000001", "テスト", "password", "TEST_ID");
		Suser suser = this.suserApplicationService.registerAdminUser(command, data -> data);

		assertThat(suser.getUserId().equals(UserId.create("1000000001"))).isTrue();
		assertThat(suser.getUsername()).isEqualTo("テスト");
		//NoOppasswordEncoderなので、暗号化されない
		assertThat(suser.getEncodedPassword()).isEqualTo("password");
		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
		assertThat(suser.getTenantUserList()).isEmpty();
		assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.ADMIN);
		assertThat(suser.getCreateId()).isEqualTo("TEST_ID");
		assertThat(suser.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();
		assertThat(suser.getVersion()).isEqualTo(1L);

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("1000000001"));
		assertThat(optUser).isPresent();
	}

	@Test
	void testRegisterMemberUser() throws DomainException {
		SuserRegisterCommand command = 
				new SuserRegisterCommand("1000000001", "テスト", "password", "TEST_ID");
		Suser suser = this.suserApplicationService
				.registerMemberUser(command, new String[] { "TEST" }, data -> data);

		assertThat(suser.getUserId().equals(UserId.create("1000000001"))).isTrue();
		assertThat(suser.getUsername()).isEqualTo("テスト");
		//NoOppasswordEncoderなので、暗号化されない
		assertThat(suser.getEncodedPassword()).isEqualTo("password");
		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
		assertThat(suser.getTenantUserList()).hasSize(1);
		assertThat(suser.getTenantUserList().get(0).getTenantId().equals(TenantId.create("TEST"))).isTrue();
		assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.MEMBER);
		assertThat(suser.getCreateId()).isEqualTo("TEST_ID");
		assertThat(suser.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();
		assertThat(suser.getVersion()).isEqualTo(1L);

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("1000000001"));
		assertThat(optUser).isPresent();
	}

	@Test
	void testChangeName() throws DomainException {
		SuserChangeNameCommand command = new SuserChangeNameCommand("0000000001", "テスト太郎", "TEST_ID");
		this.suserApplicationService.changeName(command, data -> data);

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("0000000001"));
		assertThat(optUser).isPresent();

		Suser user = optUser.get();
		assertThat(user.getUsername()).isEqualTo("テスト太郎");
		assertThat(user.getUpdateId()).isEqualTo("TEST_ID");
		assertThat(user.getUpdateDt()).isBefore(LocalDateTime.now());
		assertThat(user.getVersion()).isEqualTo(2L);
	}

	@Test
	void testChangePassword() throws DomainException {
		SuserChangePasswordCommand command = 
				new SuserChangePasswordCommand("0000000002", "password123", "TEST_ID");
		this.suserApplicationService.changePassword(command, data -> data);

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("0000000002"));
		assertThat(optUser).isPresent();
		Suser user = optUser.get();
		assertThat(user.getEncodedPassword()).isEqualTo("password123");
		assertThat(user.getUpdateId()).isEqualTo("TEST_ID");
		assertThat(user.getUpdateDt()).isBefore(LocalDateTime.now());
		assertThat(user.getVersion()).isEqualTo(2L);
	}

	@Test
	void testResetPasswordOf() throws DomainException {
		this.suserApplicationService.resetPasswordOf("0000000003", "TEST_ID");

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("0000000003"));
		assertThat(optUser).isPresent();

		Suser user = optUser.get();
		assertThat(user.getEncodedPassword()).isNotEqualTo("password");
		assertThat(user.getUpdateId()).isEqualTo("TEST_ID");
		assertThat(user.getUpdateDt()).isBefore(LocalDateTime.now());
		assertThat(user.getVersion()).isEqualTo(2L);
	}

	@Test
	void testGrantTenant() throws DomainException {
		SuserGrantTenantCommand command = new SuserGrantTenantCommand("0000000004", "TEST1", "TEST_ID");
		this.suserApplicationService.grantTenant(command, data -> data);

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("0000000004"));
		assertThat(optUser).isPresent();

		Suser user = optUser.get();
		assertThat(user.getTenantUserList()).hasSize(1);
		assertThat(user.getTenantUserList().get(0).getTenantId().toString()).isEqualTo("TEST1");
		assertThat(user.getUpdateId()).isNull();
		assertThat(user.getUpdateDt()).isNull();
		assertThat(user.getVersion()).isEqualTo(2L);
	}

	@Test
	void testRevokeTenant() throws DomainException {
		SuserRevokeTenantCommand command = new SuserRevokeTenantCommand("0000000005", "TEST1", "TEST_ID");
		this.suserApplicationService.revokeTenant(command, data -> data);

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("0000000005"));
		assertThat(optUser).isPresent();

		Suser user = optUser.get();
		assertThat(user.getTenantUserList()).isEmpty();
		assertThat(user.getUpdateId()).isNull();
		assertThat(user.getUpdateDt()).isNull();
		assertThat(user.getVersion()).isEqualTo(2L);
	}

	@Test
	void testCreateApiKey() throws DomainException {
		ApiKey apiKey = this.suserApplicationService.createApiKey("0000000006");

		assertThat(apiKey.toString().length()).isEqualTo(36);
	}

	@Test
	void testDeleteUserOf() throws DomainException {
		this.suserApplicationService.deleteUserOf("0000000007");

		Optional<Suser> optUser = this.suserRepository.get(UserId.create("0000000007"));
		assertThat(optUser).isEmpty();
	}

}
