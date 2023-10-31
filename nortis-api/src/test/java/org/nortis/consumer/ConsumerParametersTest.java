package org.nortis.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.nortis.consumer.parameter.ParameterDefinition;
import org.nortis.consumer.parameter.StandardParameterDefinition;
import org.nortis.consumer.parameter.Validator;
import org.nortis.consumer.parameter.converter.StringConverter;

class ConsumerParametersTest {

    @Test
    void testGetParameter_returnValue() {
        ConsumerParameters parameters = new ConsumerParameters(Maps.newHashMap("testB", "value"));

        ParameterDefinition<String> definition = new StandardParameterDefinition<>("testB", "testB", true,
                new StringConverter(), Validator.noCheck());
        assertThat(parameters.getParameter(definition)).isEqualTo("value");
    }

    @Test
    void testGetParameter_returnNull() {
        ConsumerParameters parameters = new ConsumerParameters(Maps.newHashMap("testA", "value"));

        ParameterDefinition<String> definition = new StandardParameterDefinition<>("testB", "testB", false,
                new StringConverter(), Validator.noCheck());
        assertThat(parameters.getParameter(definition)).isNull();
    }

    @Test
    void testEntrySet() {
        ConsumerParameters parameters = new ConsumerParameters(Maps.newHashMap("testA", "value"));

        assertThat(parameters.entrySet()).hasSize(1);
    }

}
