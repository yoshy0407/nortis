package org.nortis.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ApiMessageCodeTest {

    @Test
    void test10001() {
        assertThat(ApiMessageCode.NORTISAPI100001.getCode()).isEqualTo("NORTIS-API10001");
        assertThat(ApiMessageCode.NORTISAPI100001.getMessage(20))
                .isEqualTo("[NORTIS-API10001] コンシューマタイプのコード値は20文字以内で設定する必要があります");
    }

}
