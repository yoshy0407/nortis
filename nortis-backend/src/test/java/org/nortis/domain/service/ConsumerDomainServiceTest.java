package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.nortis.consumer.model.ConsumerType;
import org.nortis.consumer.parameter.StandardParameterDefinition;
import org.nortis.consumer.parameter.converter.StringConverter;
import org.nortis.consumer.parameter.validator.StringValidatiorBuilder;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.ConsumerRepository;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.RenderedMessage;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.MockMessageConsumer;

@ExtendWith(MockitoExtension.class)
class ConsumerDomainServiceTest extends TestBase {

    @Mock
    ConsumerManager consumerManager;

    @Mock
    ConsumerRepository consumerRepository;

    ConsumerDomainService consumerDomainService;

    @BeforeEach
    void setup() {
        this.consumerDomainService = new ConsumerDomainService(this.consumerManager, this.consumerRepository);
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

    @Test
    void testConsumeEvent() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        EndpointId endpointId = EndpointId.create("2000000001");

        Consumer consumer1 = Consumer.create(tenantId, ConsumerId.create("3000000001"), "テス1", "MAIL", TextType.TEXT,
                new HashMap<>());
        Consumer consumer2 = Consumer.create(tenantId, ConsumerId.create("3000000002"), "テスト2", "MAIL", TextType.HTML,
                new HashMap<>());
        List<Consumer> consumerList = Lists.list(consumer1, consumer2);
        when(this.consumerRepository.getFromEndpoint(eq(endpointId))).thenReturn(consumerList);

        MockMessageConsumer messageConsumer = new MockMessageConsumer(new ConsumerType("MAIL", "メール"),
                new ArrayList<>());
        when(this.consumerManager.getMessageConsumer(eq("MAIL"))).thenReturn(Optional.of(messageConsumer));

        RenderedMessage renderedMessage1 = new RenderedMessage(TextType.TEXT, "subject", "body");
        RenderedMessage renderedMessage2 = new RenderedMessage(TextType.HTML, "subject", "body");
        ReceiveEvent receiveEvent = ReceiveEvent.create(tenantId, endpointId, "dummyJson",
                Lists.list(renderedMessage1, renderedMessage2));

        this.consumerDomainService.consumeEvent(receiveEvent);

        verify(this.consumerManager, times(2)).handleSuccess(eq(messageConsumer), any());

    }

}
