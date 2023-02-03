package org.nortis.domain.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;

class AuthenticationTest {

	@Test
	void testCreateFromTenant() {
		Authentication auth = Authentication.createFromTenant(TenantId.create("TEST1"));
		
		assertThat(auth.getApiKey()).isNotNull();
		assertThat(auth.getTenantId()).isEqualTo(TenantId.create("TEST1"));
		assertThat(auth.getUserId()).isNull();
	}

	@Test
	void testCreateFromUserId() {
		Authentication auth = Authentication.createFromUserId(UserId.create("23456"));
		
		assertThat(auth.getApiKey()).isNotNull();
		assertThat(auth.getTenantId()).isNull();
		assertThat(auth.getUserId()).isEqualTo(UserId.create("23456"));
	}

}
