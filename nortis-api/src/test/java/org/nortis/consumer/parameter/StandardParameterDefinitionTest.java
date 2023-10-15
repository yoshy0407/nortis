package org.nortis.consumer.parameter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.nortis.consumer.parameter.converter.StringConverter;

class StandardParameterDefinitionTest {

    @Test
    void testValidate() {
        var definition = new StandardParameterDefinition<>("test", "テスト", true, new StringConverter(),
                Validator.noCheck());

        var result = definition.validate("");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).hasValue("テストが未設定です");
    }

}
