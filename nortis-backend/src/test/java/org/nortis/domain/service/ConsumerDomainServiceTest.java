package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nortis.TestBase;
import org.nortis.consumer.ConsumerManager;
import org.nortis.consumer.parameter.StandardParameterDefinition;
import org.nortis.consumer.parameter.converter.StringConverter;
import org.nortis.consumer.parameter.validator.StringValidatiorBuilder;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.MockMessageConsumer;

@ExtendWith(MockitoExtension.class)
class ConsumerDomainServiceTest extends TestBase {

    @Mock
    ConsumerManager consumerManager;

    ConsumerDomainService consumerDomainService;

    @BeforeEach
    void setup() {
        this.consumerDomainService = new ConsumerDomainService(this.consumerManager);
    }

    @Test
    void testValidateConsumerParameter_MessageConsumerNotFound() {
        var consumerType = "MAIL";

        when(this.consumerManager.getMessageConsumer(eq(consumerType))).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.consumerDomainService.validateConsumerParameter(consumerType, Collections.emptyMap());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS30005");
    }

    @Test
    void testValidateConsumerParameter_RequireParameterNotFound() {
        var consumerType = "MAIL";

        MockMessageConsumer consumer = new MockMessageConsumer(null,
                Lists.list(new StandardParameterDefinition<>("test", "テスト", true, new StringConverter(), null)));

        when(this.consumerManager.getMessageConsumer(eq(consumerType))).thenReturn(Optional.ofNullable(consumer));

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.consumerDomainService.validateConsumerParameter(consumerType, Collections.emptyMap());
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS30003");
    }

    @Test
    void testValidateConsumerParameter_ValidationError() {
        var consumerType = "MAIL";

        MockMessageConsumer consumer = new MockMessageConsumer(null,
                Lists.list(new StandardParameterDefinition<>("test", "テスト", true, new StringConverter(),
                        new StringValidatiorBuilder().notNull("errorMessage").build())));

        when(this.consumerManager.getMessageConsumer(eq(consumerType))).thenReturn(Optional.ofNullable(consumer));

        Map<String, String> parameter = Maps.newHashMap("test", null);

        DomainException ex = assertThrows(DomainException.class, () -> {
            this.consumerDomainService.validateConsumerParameter(consumerType, parameter);
        });

        assertThat(ex.getMessageId()).isEqualTo("NORTIS30003");
    }

    @Test
    void testValidateConsumerParameter_Success() {
        var consumerType = "MAIL";

        var parameter1 = new StandardParameterDefinition<>("test", "テスト", true, new StringConverter(),
                new StringValidatiorBuilder().notNull("errorMessage").build());
        var parameter2 = new StandardParameterDefinition<>("test1", "テスト1", true, new StringConverter(),
                new StringValidatiorBuilder().notNull("errorMessage1").build());

        MockMessageConsumer consumer = new MockMessageConsumer(null, Lists.list(parameter1, parameter2));

        when(this.consumerManager.getMessageConsumer(eq(consumerType))).thenReturn(Optional.ofNullable(consumer));

        Map<String, String> parameter = Maps.newHashMap("test", "value");
        parameter.put("test1", "value1");

        assertDoesNotThrow(() -> {
            this.consumerDomainService.validateConsumerParameter(consumerType, parameter);
        });

    }

}
