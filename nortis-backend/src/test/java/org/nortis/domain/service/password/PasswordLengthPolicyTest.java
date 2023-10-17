package org.nortis.domain.service.password;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;

class PasswordLengthPolicyTest extends TestBase {

    PasswordLengthPolicy policy = new PasswordLengthPolicy(8);

    @Test
    void testIsSatisfied_IsTrue() {
        assertThat(policy.isSatisfied("12345678")).isTrue();
    }

    @Test
    void testIsSatisfied_IsFalse() {
        assertThat(policy.isSatisfied("1234567")).isFalse();
    }

    @Test
    void testGetMessageCode() {
        assertThat(policy.getMessageCode().getCode()).isEqualTo("NORTIS70001");
    }

}
