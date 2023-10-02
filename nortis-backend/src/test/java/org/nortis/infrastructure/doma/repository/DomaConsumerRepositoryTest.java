package org.nortis.infrastructure.doma.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.ConsumerParameter;
import org.nortis.domain.consumer.ConsumerSubscribe;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
    "/META-INF/ddl/dropConsumer.sql",
    "/ddl/createConsumer.sql",
    "/META-INF/data/domain/del_ins_consumer.sql"
})
//@formatter:on
class DomaConsumerRepositoryTest extends RepositoryTestBase {

    @Autowired
    Entityql entityql;

    DomaConsumerRepository repository;

    @BeforeEach
    void setup() {
        this.repository = new DomaConsumerRepository(entityql);
    }

    @Test
    void testGet() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        ConsumerId consumerId = ConsumerId.create("2000000001");

        Optional<Consumer> optConsumer = this.repository.get(tenantId, consumerId);

        assertThat(optConsumer).isPresent();
        Consumer consumer = optConsumer.get();
        assertThat(consumer.getTenantId()).isEqualTo(tenantId);
        assertThat(consumer.getConsumerId()).isEqualTo(consumerId);
        assertThat(consumer.getConsumerName()).isEqualTo("テストコンシューマ1");
        assertThat(consumer.getConsumerTypeCode()).isEqualTo("MAIL");
        assertThat(consumer.getTextType()).isEqualTo(TextType.TEXT);

        Map<String, ConsumerParameter> parameters = consumer.getParameters();
        assertThat(parameters).hasSize(2);
        ConsumerParameter parameter1 = parameters.get("fromMailAddress");
        assertThat(parameter1.getConsumerId()).isEqualTo(consumerId);
        assertThat(parameter1.getParameterName()).isEqualTo("fromMailAddress");
        assertThat(parameter1.getParameterValue()).isEqualTo("noreply@nortis.org");
        ConsumerParameter parameter2 = parameters.get("sendMailAddress");
        assertThat(parameter2.getConsumerId()).isEqualTo(consumerId);
        assertThat(parameter2.getParameterName()).isEqualTo("sendMailAddress");
        assertThat(parameter2.getParameterValue()).isEqualTo("hoge@example.com,fuga@example.com,piyo@example.com");

        Map<EndpointId, ConsumerSubscribe> subscribes = consumer.getSubscribes();
        assertThat(subscribes).hasSize(2);
        ConsumerSubscribe subscribe1 = subscribes.get(EndpointId.create("3000000001"));
        assertThat(subscribe1.getConsumerId()).isEqualTo(consumerId);
        assertThat(subscribe1.getEndpointId()).isEqualTo(EndpointId.create("3000000001"));
        ConsumerSubscribe subscribe2 = subscribes.get(EndpointId.create("3000000002"));
        assertThat(subscribe2.getConsumerId()).isEqualTo(consumerId);
        assertThat(subscribe2.getEndpointId()).isEqualTo(EndpointId.create("3000000002"));

    }

    @Test
    void testGetFromEndpoint() throws DomainException {
        EndpointId endpointId = EndpointId.create("3000000001");

        List<Consumer> consumers = this.repository.getFromEndpoint(endpointId);

        assertThat(consumers).hasSize(2);
        assertThat(consumers.get(0).getConsumerId()).isEqualTo(ConsumerId.create("2000000001"));
        assertThat(consumers.get(1).getConsumerId()).isEqualTo(ConsumerId.create("2000000002"));
    }

    @Test
    void testSave() throws DomainException {
        TenantId tenantId = TenantId.create("1000000009");
        ConsumerId consumerId = ConsumerId.create("2000000009");
        EndpointId endpointId = EndpointId.create("3000000001");

        Consumer consumer = Consumer.create(tenantId, consumerId, "テスト", "MAIL", TextType.HTML,
                Maps.newHashMap("mailAddress", "hoge@example.com"));

        consumer.subscribe(endpointId);

        this.repository.save(consumer);

        Optional<Consumer> optConsumer = this.repository.get(tenantId, consumerId);
        assertThat(optConsumer).isPresent();
    }

    @Test
    void testUpdate() throws DomainException {
        TenantId tenantId = TenantId.create("1000000001");
        ConsumerId consumerId = ConsumerId.create("2000000002");

        Optional<Consumer> optConsumer = this.repository.get(tenantId, consumerId);

        Consumer consumer = optConsumer.get();
        consumer.changeName("テストテスト");

        // CONSUMER_PARAMETERの更新・登録
        Map<String, String> parameter = new HashMap<>();
        parameter.put("fromMailAddress", "fuga@example.com");
        parameter.put("test", "value");
        consumer.updateParameter(parameter);

        // CONSUMER_SUBSCRIBEの追加・削除
        consumer.subscribe(EndpointId.create("3000000009"));
        consumer.unsubscribe(EndpointId.create("3000000001"));

        this.repository.update(consumer);
    }

    @Test
    void testUpdateAll() throws DomainException {
        EndpointId endpointId = EndpointId.create("3000000001");

        List<Consumer> consumers = this.repository.getFromEndpoint(endpointId);

        consumers.get(0).changeName("テストテスト");
        consumers.get(1).changeName("テストテスト");

        // CONSUMER_PARAMETERの更新・登録
        Map<String, String> parameter = new HashMap<>();
        parameter.put("fromMailAddress", "fuga@example.com");
        parameter.put("test", "value");
        consumers.get(0).updateParameter(parameter);
        consumers.get(1).updateParameter(parameter);

        // CONSUMER_SUBSCRIBEの追加・削除
        consumers.get(0).subscribe(EndpointId.create("3000000009"));
        consumers.get(1).subscribe(EndpointId.create("3000000009"));
        consumers.get(0).unsubscribe(EndpointId.create("3000000001"));
        consumers.get(1).unsubscribe(EndpointId.create("3000000001"));

        this.repository.updateAll(consumers);
    }

    @Test
    void testRemove() throws DomainException {
        TenantId tenantId = TenantId.create("1000000002");
        ConsumerId consumerId = ConsumerId.create("2000000003");

        Optional<Consumer> optConsumer = this.repository.get(tenantId, consumerId);

        this.repository.remove(optConsumer.get());

        Optional<Consumer> optConsumer2 = this.repository.get(tenantId, consumerId);
        assertThat(optConsumer2).isEmpty();
    }

}
