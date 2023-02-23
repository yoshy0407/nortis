package org.nortis.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUser;
import org.nortis.test.MockApplicationContextAccessor;

class AuthorityCheckDomainServiceTest {

	SuserRepository suserRepository;
	
	AuthorityCheckDomainService domainService;
	
	@BeforeEach
	void setup() {
		this.suserRepository = Mockito.mock(SuserRepository.class);
		this.domainService = new AuthorityCheckDomainService(this.suserRepository);
		
		var mockAccessor = new MockApplicationContextAccessor();
		mockAccessor.mockTestPasswordEncoder();
	}
	
	@Test
	void testCheckAdminOfSuccess() throws DomainException {
		UserId userId = UserId.create("0000000001");
		
		Suser suser = Suser.createAdmin(userId, "テスト", "password", "TEST_ID");
		
		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.of(suser));
		
		assertDoesNotThrow(() -> {
			this.domainService.checkAdminOf(userId);
		});
	}

	@Test
	void testCheckAdminOfUserNotFound() throws DomainException {
		UserId userId = UserId.create("0000000001");
		
		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.empty());
		
		DomainException ex = assertThrows(DomainException.class, () -> {
			this.domainService.checkAdminOf(userId);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS00003");
	}

	@Test
	void testCheckAdminOfNotAdmin() throws DomainException {
		UserId userId = UserId.create("0000000001");
		
		Suser suser = Suser.createMember(userId, "テスト", "password", Lists.newArrayList(), "TEST_ID");	

		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.of(suser));
		
		DomainException ex = assertThrows(DomainException.class, () -> {
			this.domainService.checkAdminOf(userId);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
	}

	@Test
	void testCheckTenantAuthoritySuccessWithAdmin() throws DomainException {
		
		UserId userId = UserId.create("0000000001");
		Suser suser = Suser.createAdmin(userId, "テスト", "password", "TEST_ID");		
		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.of(suser));

		NortisUser user = 
				new NortisUser(userId.toString(), new String[] {"TEST"}, 
						"password", false, Lists.newArrayList(), false);
		Tenant tenant = Tenant.create(TenantId.create("TEST"), "テストテナント", "TEST_ID");
		assertDoesNotThrow(() -> {
			this.domainService.checkTenantAuthority(user, tenant);
		});
		
	}

	@Test
	void testCheckTenantAuthoritySuccessWithSameTenant() throws DomainException {
		UserId userId = UserId.create("0000000001");
		TenantId tenantId = TenantId.create("TEST");
		Suser suser = Suser.createMember(userId, "テスト", "password", Lists.list(tenantId), "TEST_ID");		
		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.of(suser));

		NortisUser user = 
				new NortisUser(userId.toString(), new String[] {"TEST"}, 
						"password", false, Lists.newArrayList(), false);
		Tenant tenant = Tenant.create(tenantId, "テストテナント", "TEST_ID");
		assertDoesNotThrow(() -> {
			this.domainService.checkTenantAuthority(user, tenant);
		});
	}

	@Test
	void testCheckTenantAuthorityFailUserNotFound() throws DomainException {
		UserId userId = UserId.create("0000000001");
		TenantId tenantId = TenantId.create("TEST");
		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.empty());

		NortisUser user = 
				new NortisUser(userId.toString(), new String[] {"TEST"}, 
						"password", false, Lists.newArrayList(), false);
		Tenant tenant = Tenant.create(tenantId, "テストテナント", "TEST_ID");
		DomainException ex = assertThrows(DomainException.class, () -> {
			this.domainService.checkTenantAuthority(user, tenant);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS00003");
	}
	
	@Test
	void testCheckTenantAuthorityFailNotSameTenant() throws DomainException {
		UserId userId = UserId.create("0000000001");
		TenantId tenantId = TenantId.create("HOGE");
		Suser suser = Suser.createMember(userId, "テスト", "password", Lists.list(TenantId.create("TEST")), "TEST_ID");		
		Mockito.when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.of(suser));

		NortisUser user = 
				new NortisUser(userId.toString(), new String[] {"HOGE"}, 
						"password", false, Lists.newArrayList(), false);
		Tenant tenant = Tenant.create(tenantId, "テストテナント", "TEST_ID");
		DomainException ex = assertThrows(DomainException.class, () -> {
			this.domainService.checkTenantAuthority(user, tenant);
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS50005");
	}

}
