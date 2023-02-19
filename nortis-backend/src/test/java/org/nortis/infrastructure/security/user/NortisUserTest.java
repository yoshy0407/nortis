package org.nortis.infrastructure.security.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.MockApplicationContextAccessor;

class NortisUserTest {

	@BeforeEach
	void setup() {
		MockApplicationContextAccessor accessor = new MockApplicationContextAccessor();
		accessor.mockTestPasswordEncoder();
	}
	
	@Test
	void testCreateOfTenant() throws DomainException {
		Authentication auth = Authentication.createFromTenant(TenantId.create("TEST"));
		NortisUser user = NortisUser.createOfTenant(auth, false);
		
		assertThat(user.getUsername()).isEqualTo("TEST");
		assertThat(user.getPassword()).isNotEmpty();
		assertThat(user.getTenantId()).hasSize(1);
		assertThat(user.getTenantId()[0]).isEqualTo("TEST");
		assertThat(user.getAuthorities()).hasSize(1);
		assertThat(user.isEnabled()).isTrue();
		assertThat(user.isAccountNonExpired()).isTrue();
		assertThat(user.isAccountNonLocked()).isTrue();
		assertThat(user.isCredentialsNonExpired()).isTrue();
	}

	@Test
	void testCreateOfUser() throws DomainException {
		UserId userId = UserId.create("0000000001");
		Authentication auth = Authentication.createFromUserId(userId);
		Suser suser = 
				Suser.createMember(userId, "テスト", "password", Lists.newArrayList(TenantId.create("TEST")), "TEST_ID");
		NortisUser user = NortisUser.createOfUser(auth, suser, true);
		
		assertThat(user.getUsername()).isEqualTo("0000000001");
		assertThat(user.getPassword()).isNotEmpty();
		assertThat(user.getTenantId()).hasSize(1);
		assertThat(user.getTenantId()[0]).isEqualTo("TEST");
		assertThat(user.getAuthorities()).hasSize(1);
		assertThat(user.isEnabled()).isTrue();
		assertThat(user.isAccountNonExpired()).isTrue();
		assertThat(user.isAccountNonLocked()).isTrue();
		assertThat(user.isCredentialsNonExpired()).isFalse();
	}

}
