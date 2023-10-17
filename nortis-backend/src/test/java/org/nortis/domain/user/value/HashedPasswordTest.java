package org.nortis.domain.user.value;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class HashedPasswordTest {

    @Test
    void testHashCode() {
        HashedPassword password1 = HashedPassword.create("password1");
        HashedPassword password2 = HashedPassword.create("password1");
        HashedPassword password3 = HashedPassword.create("password123");

        assertThat(password1.hashCode()).isEqualTo(password2.hashCode());
        assertThat(password1.hashCode()).isNotEqualTo(password3.hashCode());
    }

    @Test
    void testToString() {
        assertThat(HashedPassword.create("password").toString()).isEqualTo("password");
    }

    @Test
    void testEqualsObject() {
        HashedPassword password1 = HashedPassword.create("password1");
        HashedPassword password2 = HashedPassword.create("password1");
        HashedPassword password3 = HashedPassword.create("password123");

        assertThat(password1.equals(password2)).isTrue();
        assertThat(password1.equals(password3)).isFalse();
    }

}
