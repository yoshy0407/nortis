package org.nortis.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.UserId;
import org.nortis.test.MockApplicationContextAccessor;

class SuserTest {

	@BeforeEach
	void setup() {
		MockApplicationContextAccessor accessor = new MockApplicationContextAccessor();
		accessor.mockTestPasswordEncoder();
	}
	
	@Test
	void testChangeUsername() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		assertThat(suser.getUsername()).isEqualTo("テストユーザ");
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();

		suser.changeUsername("サンプルユーザ", "USER_ID");
		assertThat(suser.getUsername()).isEqualTo("サンプルユーザ");
		assertThat(suser.getUpdateId()).isEqualTo("USER_ID");
		assertThat(suser.getUpdateDt()).isBefore(LocalDateTime.now());
	}

	@Test
	void testChangePassword() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		assertThat(suser.getEncodedPassword()).isEqualTo("encodedPassword");
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();

		suser.changePassword("password", "USER_ID");
		assertThat(suser.getEncodedPassword()).isEqualTo("password");
		assertThat(suser.getUpdateId()).isEqualTo("USER_ID");
		assertThat(suser.getUpdateDt()).isBefore(LocalDateTime.now());
	}

	@Test
	void testResetPassword() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		assertThat(suser.getEncodedPassword()).isEqualTo("encodedPassword");
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();

		String password = suser.resetPassword("ADMIN");
		assertThat(password).hasSize(15);
		assertThat(suser.getEncodedPassword()).hasSize(15);
		assertThat(suser.getUpdateId()).isEqualTo("ADMIN");
		assertThat(suser.getUpdateDt()).isBefore(LocalDateTime.now());
	}

	@Test
	void testLogin() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();

		suser.login();

		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.LOGIN);
		assertThat(suser.getUpdateId()).isEqualTo("0000000001");
		assertThat(suser.getUpdateDt()).isBefore(LocalDateTime.now());
	}

	@Test
	void testLogout() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		suser.login();

		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.LOGIN);

		suser.logout();

		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
	}

	@Test
	void testCreateApiKey() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		Authentication authentication = suser.createApiKey();

		assertThat(authentication.getApiKey()).isNotNull();
		assertThat(authentication.getTenantId()).isNull();
		assertThat(authentication.getUserId()).isEqualTo(UserId.create("0000000001"));
	}

	@Test
	void testGrantTenantAccressOf() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		suser.grantTenantAccressOf(TenantId.create("TEST4"));

		assertThat(suser.getTenantUserList()).hasSize(3);
		Optional<TenantUser> optTenantUser = suser.getTenantUserList().stream()
				.filter(data -> data.getTenantId().equals(TenantId.create("TEST4")))
				.findFirst();
		
		assertThat(optTenantUser).isPresent();
		assertThat(optTenantUser.get().getUserId()).isEqualTo(UserId.create("0000000001"));
		assertThat(optTenantUser.get().getTenantId()).isEqualTo(TenantId.create("TEST4"));
		assertThat(optTenantUser.get().isInsert()).isTrue();
	}

	@Test
	void testRevokeTenantAccessOf() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		suser.revokeTenantAccessOf(TenantId.create("TEST2"));

		assertThat(suser.getTenantUserList()).hasSize(2);

		Optional<TenantUser> optTenantUser = suser.getTenantUserList().stream()
				.filter(data -> data.getTenantId().equals(TenantId.create("TEST2")))
				.findFirst();
		
		assertThat(optTenantUser).isPresent();
		assertThat(optTenantUser.get().isDeleted()).isTrue();
	}

	@Test
	void testCreateMember() {
		Suser suser = Suser.createMember(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				Lists.list(TenantId.create("TEST1"), TenantId.create("TEST2")),
				"TEST_ID");

		assertThat(suser.getUserId()).isEqualTo(UserId.create("0000000001"));
		assertThat(suser.getUsername()).isEqualTo("テストユーザ");
		assertThat(suser.getEncodedPassword()).isEqualTo("encodedPassword");
		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);

		assertThat(suser.getTenantUserList()).hasSize(2);
		TenantUser tenantUser1 = suser.getTenantUserList().get(0);
		assertThat(tenantUser1.getUserId()).isEqualTo(UserId.create("0000000001"));
		assertThat(tenantUser1.getTenantId()).isEqualTo(TenantId.create("TEST1"));

		TenantUser tenantUser2 = suser.getTenantUserList().get(1);
		assertThat(tenantUser2.getUserId()).isEqualTo(UserId.create("0000000001"));
		assertThat(tenantUser2.getTenantId()).isEqualTo(TenantId.create("TEST2"));

		assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.MEMBER);
		assertThat(suser.getCreateId()).isEqualTo("TEST_ID");
		assertThat(suser.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();
		assertThat(suser.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateAdmin() {
		Suser suser = Suser.createAdmin(
				UserId.create("0000000001"), 
				"テストユーザ",
				"encodedPassword",
				"TEST_ID");

		assertThat(suser.getUserId()).isEqualTo(UserId.create("0000000001"));
		assertThat(suser.getUsername()).isEqualTo("テストユーザ");
		assertThat(suser.getEncodedPassword()).isEqualTo("encodedPassword");
		assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
		assertThat(suser.getTenantUserList()).isEmpty();
		assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.ADMIN);
		assertThat(suser.getCreateId()).isEqualTo("TEST_ID");
		assertThat(suser.getCreateDt()).isBefore(LocalDateTime.now());
		assertThat(suser.getUpdateId()).isNull();
		assertThat(suser.getUpdateDt()).isNull();
		assertThat(suser.getVersion()).isEqualTo(1L);
	}

}
