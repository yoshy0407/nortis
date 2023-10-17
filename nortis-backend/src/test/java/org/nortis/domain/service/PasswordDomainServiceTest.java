package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;
import org.nortis.domain.service.password.PasswordPolicy;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCode;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@SuppressWarnings("deprecation")
class PasswordDomainServiceTest extends TestBase {

    PasswordDomainService domainService;

    @BeforeEach
    void setup() {
        this.domainService = new PasswordDomainService(NoOpPasswordEncoder.getInstance());
    }

    @Test
    void testCheckPolicyOf_CheckSuccessWithPolicy() {
        this.domainService.addPolicy(new TestPasswordPolicy("password"));

        assertDoesNotThrow(() -> {
            this.domainService.checkPolicyOf("password");
        });
    }

    @Test
    void testCheckPolicyOf_CheckSuccessNoPolicy() {
        assertDoesNotThrow(() -> {
            this.domainService.checkPolicyOf("password");
        });
    }

    @Test
    void testCheckPolicyOf_CheckFailure() {
        this.domainService.addPolicy(new TestPasswordPolicy("password"));

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.domainService.checkPolicyOf("password123");
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS00500");
    }

    @Test
    void testHashPassword() {
        HashedPassword hashedPassword = this.domainService.hashPassword("password");
        assertThat(hashedPassword).isEqualTo(HashedPassword.create("password"));
    }

    @Test
    void testMatch_Success() {
        boolean result = this.domainService.match("password", HashedPassword.create("password"));
        assertThat(result).isTrue();
    }

    @Test
    void testMatch_Failure() {
        boolean result = this.domainService.match("password", HashedPassword.create("password123"));
        assertThat(result).isFalse();
    }

    @Test
    void testCreateResetPassword() {
        String resetPassword = this.domainService.createResetPassword();
        assertThat(resetPassword).isNotBlank();
    }

    @AllArgsConstructor
    class TestPasswordPolicy implements PasswordPolicy {

        private final String test;

        @Override
        public boolean isSatisfied(String rawPassword) {
            return rawPassword.equals(test);
        }

        @Override
        public MessageCode getMessageCode() {
            return MessageCodes.nortis00500();
        }

    }

}
