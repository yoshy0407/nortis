package org.nortis.domain.user.value;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class AdminFlgTest {

    @Test
    void testGetValue() {
        assertThat(AdminFlg.ADMIN.getValue()).isEqualTo("1");
        assertThat(AdminFlg.MEMBER.getValue()).isEqualTo("0");
    }

    @Test
    void testGetDisplayName() {
        assertThat(AdminFlg.ADMIN.getDisplayName()).isEqualTo("管理者ユーザ");
        assertThat(AdminFlg.MEMBER.getDisplayName()).isEqualTo("通常ユーザ");
    }

    @Test
    void testResolve() {
        assertThat(AdminFlg.resolve("1")).isEqualTo(AdminFlg.ADMIN);
        assertThat(AdminFlg.resolve("0")).isEqualTo(AdminFlg.MEMBER);
    }

}
