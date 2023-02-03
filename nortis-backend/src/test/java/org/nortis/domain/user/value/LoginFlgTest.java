package org.nortis.domain.user.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LoginFlgTest {

	@Test
	void testGetValue() {
		assertThat(LoginFlg.LOGIN.getValue()).isEqualTo("1");
		assertThat(LoginFlg.NOT_LOGIN.getValue()).isEqualTo("0");
	}

	@Test
	void testResolve() {
		assertThat(LoginFlg.resolve("1")).isEqualTo(LoginFlg.LOGIN);
		assertThat(LoginFlg.resolve("0")).isEqualTo(LoginFlg.NOT_LOGIN);
	}

}
