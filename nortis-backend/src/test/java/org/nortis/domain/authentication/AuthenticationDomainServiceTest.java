package org.nortis.domain.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.MockApplicationContextAccessor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;



@SuppressWarnings("deprecation")
class AuthenticationDomainServiceTest {

	AuthenticationDomainService domainService;

	AuthenticationRepository authenticationRepository;
	
	SuserRepository suserRepository;
	
	@BeforeEach
	void setup() {
		MockApplicationContextAccessor accessor = new MockApplicationContextAccessor();
		accessor.mockTestPasswordEncoder();

		this.authenticationRepository = Mockito.mock(AuthenticationRepository.class);
		this.suserRepository = Mockito.mock(SuserRepository.class);
		this.domainService = new AuthenticationDomainService(
				authenticationRepository, 
				suserRepository,
				NoOpPasswordEncoder.getInstance(),
				600);
	}

	@Test
	void testCreateApiKeyOfTenant() throws DomainException {
		Tenant tenant = Tenant.create(
				TenantId.create("TEST"), 
				"Test Tenant", 
				"TEST_ID");

		when(this.authenticationRepository.getFromTenantId(eq(TenantId.create("TEST"))))
		.thenReturn(Optional.empty());

		ApiKey apiKey = this.domainService.createApiKeyOf(tenant);

		assertThat(apiKey.toString()).hasSize(36);

		ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
		verify(this.authenticationRepository).save(captor.capture());

		Authentication authentication = captor.getValue();

		assertThat(authentication.getApiKey()).isNotNull();
		assertThat(authentication.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(authentication.getUserId()).isNull();
	}

	@Test
	void testCreateApiKeyOfTenantPresent() throws DomainException {
		Tenant tenant = Tenant.create(
				TenantId.create("TEST"), 
				"Test Tenant", 
				"TEST_ID");

		Authentication present = Authentication.createFromTenant(TenantId.create("TEST"));
		when(this.authenticationRepository.getFromTenantId(eq(TenantId.create("TEST"))))
		.thenReturn(Optional.of(present));

		ApiKey apiKey = this.domainService.createApiKeyOf(tenant);

		assertThat(apiKey.toString()).hasSize(36);

		verify(this.authenticationRepository).remove(eq(present));

		ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
		verify(this.authenticationRepository).save(captor.capture());

		Authentication authentication = captor.getValue();

		assertThat(authentication.getApiKey()).isNotNull();
		assertThat(authentication.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(authentication.getUserId()).isNull();
	}

	@Test
	void testCreateApiKeyOfSuser() throws DomainException {
		Suser suser = Suser.createMember(
				UserId.create("1000000001"), 
				"Test User", 
				"encodedPassword", 
				Lists.newArrayList(TenantId.create("TEST")), 
				"TEST_ID");

		when(this.authenticationRepository.getFromUserId(eq(UserId.create("1000000001"))))
		.thenReturn(Optional.empty());

		ApiKey apiKey = this.domainService.createApiKeyOf(suser);

		assertThat(apiKey.toString()).hasSize(36);

		ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
		verify(this.authenticationRepository).save(captor.capture());

		Authentication authentication = captor.getValue();

		assertThat(authentication.getApiKey()).isNotNull();
		assertThat(authentication.getTenantId()).isNull();
		assertThat(authentication.getUserId()).isEqualTo(UserId.create("1000000001"));
	}

	@Test
	void testCreateApiKeyOfSuserPresent() throws DomainException {
		Suser suser = Suser.createMember(
				UserId.create("1000000001"), 
				"Test User", 
				"encodedPassword", 
				Lists.newArrayList(TenantId.create("TEST")), 
				"TEST_ID");

		Authentication present = Authentication.createFromTenant(TenantId.create("TEST"));
		when(this.authenticationRepository.getFromUserId(eq(UserId.create("1000000001"))))
		.thenReturn(Optional.of(present));

		ApiKey apiKey = this.domainService.createApiKeyOf(suser);

		assertThat(apiKey.toString()).hasSize(36);

		verify(this.authenticationRepository).remove(eq(present));

		ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
		verify(this.authenticationRepository).save(captor.capture());

		Authentication authentication = captor.getValue();

		assertThat(authentication.getApiKey()).isNotNull();
		assertThat(authentication.getTenantId()).isNull();
		assertThat(authentication.getUserId()).isEqualTo(UserId.create("1000000001"));
	}

	@Test
	void testCheckExpiredTenant() throws DomainException {
		Authentication auth = Authentication.createFromTenant(TenantId.create("TENANT"));
		auth.setLastAccessDatetime(LocalDateTime.now());
		assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isFalse();
	}

	@Test
	void testCheckExpiredUserNotExpire() throws DomainException {
		Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
		auth.setLastAccessDatetime(LocalDateTime.now());		
		assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isFalse();
	}

	@Test
	void testCheckExpiredUserExpire() throws DomainException {
		Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
		auth.setLastAccessDatetime(LocalDateTime.now().minusSeconds(1200));		
		assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isTrue();
	}

	@Test
	void testCheckExpiredNull() throws DomainException {
		Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
		assertThat(this.domainService.checkExpired(auth, LocalDateTime.now())).isFalse();
	}

	@Test
	void testAuthorizeOfUserSuccess() throws DomainException {
		UserId userId = UserId.create("0000000001");
		Authentication auth = Authentication.createFromUserId(userId);

		when(this.authenticationRepository.get(eq(auth.getApiKey())))
		.thenReturn(Optional.ofNullable(auth));

		Suser suser = Suser.createMember(
				userId, "テスト", "password", Lists.newArrayList(TenantId.create("TEST")), "TEST_ID");
		
		when(this.suserRepository.get(eq(userId)))
			.thenReturn(Optional.of(suser));
		
		assertDoesNotThrow(() -> {
			this.domainService.authorizeOfApiKey(auth.getApiKey());			
		});
		assertThat(auth.getLastAccessDatetime()).isNotNull();
	}

	@Test
	void testAuthorizeOfUserFailure() throws DomainException {
		ApiKey apiKey = ApiKey.newKey();
		when(this.authenticationRepository.get(eq(apiKey)))
		.thenReturn(Optional.empty());

		assertThrows(DomainException.class, () -> {
			this.domainService.authorizeOfApiKey(apiKey);						
		});
	}

	@Test
	void testAuthorizeOfTenantSuccess() throws DomainException {
		Authentication auth = Authentication.createFromTenant(TenantId.create("TEST"));

		when(this.authenticationRepository.get(eq(auth.getApiKey())))
		.thenReturn(Optional.ofNullable(auth));

		assertDoesNotThrow(() -> {
			this.domainService.authorizeOfApiKey(auth.getApiKey());			
		});
		assertThat(auth.getLastAccessDatetime()).isNotNull();
	}

	@Test
	void testAuthorizeOfUserExpiredFailure() throws DomainException {
		Authentication auth = Authentication.createFromUserId(UserId.create("0000000001"));
		auth.setLastAccessDatetime(LocalDateTime.now().minusSeconds(1200));

		when(this.authenticationRepository.get(eq(auth.getApiKey())))
			.thenReturn(Optional.ofNullable(auth));

		assertThrows(DomainException.class, () -> {
			this.domainService.authorizeOfApiKey(auth.getApiKey());			
		});
	}
	
	@Test
	void testLogin() throws DomainException {
		UserId userId = UserId.create("0000000001");
		Suser suser = Suser.createMember(
				userId, 
				"JIM", 
				"password", 
				Lists.list(TenantId.create("TEST")), "TEST_ID");
		
		when(this.suserRepository.get(Mockito.eq(userId)))
			.thenReturn(Optional.of(suser));
		
		domainService.login(UserId.create("0000000001"), "password");
		
		ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
		verify(this.authenticationRepository).save(captor.capture());
		
		Authentication auth = captor.getValue();
		assertThat(auth.getUserId()).isEqualTo(userId);
	}
	
	@Test
	void testLoginUserNotFound() throws DomainException {
		UserId userId = UserId.create("0000000001");
		
		when(this.suserRepository.get(Mockito.eq(userId)))
			.thenReturn(Optional.empty());
		
		DomainException ex = assertThrows(DomainException.class, () -> {
			domainService.login(UserId.create("0000000001"), "password");			
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS50004");
	}

	@Test
	void testLoginPasswordNotMatch() throws DomainException {
		UserId userId = UserId.create("0000000001");
		Suser suser = Suser.createMember(
				userId, 
				"JIM", 
				"test", 
				Lists.list(TenantId.create("TEST")), "TEST_ID");
		
		when(this.suserRepository.get(Mockito.eq(userId)))
			.thenReturn(Optional.of(suser));
		
		DomainException ex = assertThrows(DomainException.class, () -> {
			domainService.login(UserId.create("0000000001"), "password");			
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS50004");
	}
	
	@Test
	void testLogout() throws DomainException {
		UserId userId = UserId.create("0000000001");
		Suser suser = Suser.createMember(
				userId, 
				"JIM", 
				"test", 
				Lists.list(TenantId.create("TEST")), "TEST_ID");

		when(this.suserRepository.get(Mockito.eq(userId)))
			.thenReturn(Optional.of(suser));
		
		Authentication auth = Authentication.createFromUserId(userId);
		when(this.authenticationRepository.getFromUserId(Mockito.eq(userId)))
			.thenReturn(Optional.of(auth));
		
		domainService.logout(userId);
		
		ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
		verify(this.authenticationRepository).remove(captor.capture());

		assertThat(captor.getValue()).isEqualTo(auth);
	}
	
	@Test
	void testLogoutUserNotFound() throws DomainException {
		UserId userId = UserId.create("0000000001");
		when(this.suserRepository.get(Mockito.eq(userId)))
			.thenReturn(Optional.empty());
		
		DomainException ex = assertThrows(DomainException.class, () -> {
			domainService.logout(userId);			
		});
		
		assertThat(ex.getMessageId()).isEqualTo("NORTIS00003");
	}

}
