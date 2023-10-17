package org.nortis.domain.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.nortis.consumer.parameter.StandardParameterDefinition;
import org.nortis.consumer.parameter.converter.StringConverter;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;

class ConsumerTest {

    @Test
    void testAddParameter() throws DomainException {
        TenantId tenantId = TenantId.create("0000000001");
        ConsumerId consumerId = ConsumerId.create("1000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テスト", "MAIL", TextType.HTML, Collections.emptyMap());

        assertThat(consumer.getParameters()).hasSize(0);

        consumer.addParameter("test", "value");

        assertThat(consumer.getParameters()).hasSize(1);
        assertThat(consumer.getParameters().get("test").getParameterValue()).isEqualTo("value");
    }

    @Test
    void testUpdateParameter() throws DomainException {

        TenantId tenantId = TenantId.create("0000000001");
        ConsumerId consumerId = ConsumerId.create("1000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テスト", "MAIL", TextType.HTML, Collections.emptyMap());
        consumer.addParameter("test", "value");

        Map<String, String> parameter = new HashMap<>();
        parameter.put("test", "testValue");
        parameter.put("test1", "test1");

        consumer.updateParameter(parameter);

        assertThat(consumer.getParameters()).hasSize(2);
        assertThat(consumer.getParameters().get("test").getParameterValue()).isEqualTo("testValue");
        assertThat(consumer.getParameters().get("test1").getParameterValue()).isEqualTo("test1");
    }

    @Test
    void testSubscribe() throws DomainException {

        TenantId tenantId = TenantId.create("0000000001");
        ConsumerId consumerId = ConsumerId.create("1000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テスト", "MAIL", TextType.HTML, Collections.emptyMap());

        consumer.subscribe(EndpointId.create("2000000001"));

        assertThat(consumer.getSubscribes()).hasSize(1);
        assertThat(consumer.getSubscribes()).containsKey(EndpointId.create("2000000001"));

    }

    @Test
    void testUnsubscribe() throws DomainException {

        TenantId tenantId = TenantId.create("0000000001");
        ConsumerId consumerId = ConsumerId.create("1000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テスト", "MAIL", TextType.HTML, Collections.emptyMap());

        var endpointId = EndpointId.create("2000000001");

        consumer.subscribe(endpointId);

        consumer.unsubscribe(endpointId);

        assertThat(consumer.getSubscribes()).hasSize(1);
        assertThat(consumer.getSubscribes().get(endpointId).isDelete()).isTrue();
    }

    @Test
    void testGetParameter() throws DomainException {

        TenantId tenantId = TenantId.create("0000000001");
        ConsumerId consumerId = ConsumerId.create("1000000001");
        Consumer consumer = Consumer.create(tenantId, consumerId, "テスト", "MAIL", TextType.HTML, Collections.emptyMap());
        consumer.addParameter("test", "value");

        String text = consumer.getParameter()
                .getParameter(new StandardParameterDefinition<>("test", null, false, new StringConverter(), null));
        assertThat(text).isEqualTo("value");
    }

}
