package org.nortis.consumer.parameter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.nortis.consumer.parameter.converter.LongConverter;
import org.nortis.consumer.parameter.converter.StringConverter;

class StandardParameterDefinitionTest {

    StandardParameterDefinition<Long> definition = new StandardParameterDefinition<>("test", "テスト", true,
            new LongConverter(), Validator.noCheck());

    @Test
    void testGetDisplayName() {
        assertThat(definition.getDisplayName()).isEqualTo("テスト");
    }

    @Test
    void testGetParameterName() {
        assertThat(definition.getParameterName()).isEqualTo("test");
    }

    @Test
    void testSerialize() {
        assertThat(definition.serialize(100L)).isEqualTo("100");
    }

    @Test
    void testDeserialize() {
        assertThat(definition.deserialize("234")).isEqualTo(234L);
    }

    @Test
    void testValidate() {
        var definition = new StandardParameterDefinition<>("test", "テスト", true, new StringConverter(),
                Validator.noCheck());

        var result = definition.validate("");

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).hasValue("テストが未設定です");
    }

}
