package org.nortis.application.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.config.DomaConfiguration;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {
		"/META-INF/ddl/dropAuthentication.sql",
		"/META-INF/ddl/dropSuser.sql",
		"/ddl/createAuthentication.sql",
		"/ddl/createSuser.sql",
		"/META-INF/data/application/del_ins_authentication.sql",
		"/META-INF/data/application/del_ins_suser.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = { 
		DomaAutoConfiguration.class, 
		DomaConfiguration.class, 
		AuthenticationApplicationServiceTestConfig.class
	})
class AuthenticationApplicationServiceTest {

	@Autowired
	AuthenticationApplicationService applicationService;
	
	@Autowired
	AuthenticationRepository authenticationRepository;
	
	@Test
	void testLogin() {
		ApiKey apiKey = this.applicationService.login("0000000001", "password", auth -> auth.getApiKey());
		
		Optional<Authentication> optAuth = this.authenticationRepository.get(apiKey);
		
		assertThat(optAuth).isPresent();
	}

	@Test
	void testLogout() {
		this.applicationService.logout("0000000009");

		Optional<Authentication> optAuth = 
				this.authenticationRepository.getFromUserId(UserId.create("0000000009"));
		
		assertThat(optAuth).isEmpty();
	}

	@Test
	void testAuthenticateOfTenant() {
		assertDoesNotThrow(() -> {
			NortisUserDetails user = this.applicationService.authenticateOf("APIKEYTENANT1");
			
			assertThat(user.getUsername()).isEqualTo("TEST");
			assertThat(user.getPassword()).isEqualTo("APIKEYTENANT1");
			assertThat(user.getAuthorities()).hasSize(1);
			assertThat(user.getAuthorities()).anySatisfy(auth -> {
				assertThat(auth.getAuthority()).isEqualTo(AdminFlg.MEMBER.name());
			});
			assertThat(user.getTenantId()).anySatisfy(id -> {
				assertThat(id).isEqualTo("TEST");
			});
		});
	}

	@Test
	void testAuthenticateOfUser() {
		assertDoesNotThrow(() -> {
			NortisUserDetails user = this.applicationService.authenticateOf("APIKEYUSER3");
			
			assertThat(user.getUsername()).isEqualTo("0000000007");
			assertThat(user.getPassword()).isEqualTo("APIKEYUSER3");
			assertThat(user.getAuthorities()).hasSize(1);
			assertThat(user.getAuthorities()).anySatisfy(auth -> {
				assertThat(auth.getAuthority()).isEqualTo(AdminFlg.ADMIN.name());
			});
			assertThat(user.getTenantId()).isEmpty();
		});
	}

	@Test
	void testRemoveExpiredAuthentication() {
		this.applicationService.removeExpiredAuthentication(LocalDateTime.of(2022, 1, 5, 12, 40, 00));
		
		List<Authentication> list = this.authenticationRepository.getUserAuthentication();
		
		assertThat(list).hasSize(2);
		assertThat(list.get(0).getApiKey()).isEqualTo(ApiKey.create("APIKEYUSER2"));
		assertThat(list.get(1).getApiKey()).isEqualTo(ApiKey.create("APIKEYUSER3"));
	}

}
