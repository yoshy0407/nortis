package org.nortis.domain.tenant.value;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.nortis.TestBase;

class OperationIdTest extends TestBase {

    @Test
    void testResolve() {
        var operationId = OperationId.resolve("00101");
        assertThat(operationId).isEqualTo(OperationId.READ_ENDPOINT);
    }

    @Test
    void testGetValue() {
        assertThat(OperationId.READ_ENDPOINT.getValue()).isEqualTo("00101");
    }

    @Test
    void testGetDisplayName() {
        assertThat(OperationId.READ_ENDPOINT.getDisplayName()).isEqualTo("エンドポイント読み取り権限");
    }

}
