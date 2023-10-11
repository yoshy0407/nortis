package org.nortis.infrastructure.doma.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.ConsumerParameter;
import org.nortis.domain.consumer.ConsumerParameter_;
import org.nortis.domain.consumer.ConsumerRepository;
import org.nortis.domain.consumer.ConsumerSubscribe;
import org.nortis.domain.consumer.ConsumerSubscribe_;
import org.nortis.domain.consumer.Consumer_;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.application.Paging;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStarting;
import org.springframework.stereotype.Repository;

/**
 * {@link ConsumerRepository}のDomaの実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Repository
public class DomaConsumerRepository implements ConsumerRepository {

    private final Entityql entityql;

    private final Consumer_ consumer = new Consumer_();

    private final ConsumerSubscribe_ consumerSubscribe = new ConsumerSubscribe_();

    private final ConsumerParameter_ consumerParameter = new ConsumerParameter_();

    @Override
    public Optional<Consumer> get(TenantId tenantId, ConsumerId consumerId) {
        //@formatter:off
        return select()
                .where(c -> {
                    c.eq(this.consumer.tenantId, tenantId);
                    c.eq(this.consumer.consumerId, consumerId);
                })
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public List<Consumer> getListPaging(TenantId tenantId, Paging paging) {
        //@formatter:off
        return select()
                .orderBy(order -> order.asc(this.consumer.consumerId))
                .offset(paging.offset())
                .limit(paging.limit())
                .fetch();
        //@formatter:on
    }

    @Override
    public List<Consumer> getFromEndpoint(EndpointId endpointId) {
        //@formatter:off
        return select()
                .where(c -> {
                    c.eq(this.consumerSubscribe.endpointId, endpointId);
                })
                .orderBy(order -> {
                    order.asc(this.consumer.consumerId);
                })
                .fetch();
        //@formatter:on
    }

    @Override
    public void save(Consumer consumer) {
        this.entityql.insert(this.consumer, consumer).execute();
        this.entityql.insert(this.consumerParameter, consumer.getParameters().values().stream().toList()).execute();
        this.entityql.insert(this.consumerSubscribe, consumer.getSubscribes().values().stream().toList()).execute();
    }

    @Override
    public void update(Consumer consumer) {
        this.entityql.update(this.consumer, consumer).execute();

        // CONSUMER_PARAMETERの更新
        for (ConsumerParameter consumerParameter : consumer.getParameters().values()) {
            if (consumerParameter.isDelete()) {
                this.entityql.delete(this.consumerParameter, consumerParameter).execute();
            }
            if (consumerParameter.isUpdate()) {
                this.entityql.update(this.consumerParameter, consumerParameter).execute();
            }
            if (consumerParameter.isInsert()) {
                this.entityql.insert(this.consumerParameter, consumerParameter).execute();
            }
        }

        // CONSUMER_SUBSCRIBEの更新
        for (ConsumerSubscribe consumerSubscribe : consumer.getSubscribes().values()) {
            if (consumerSubscribe.isDelete()) {
                this.entityql.delete(this.consumerSubscribe, consumerSubscribe).execute();
            }
            if (consumerSubscribe.isInsert()) {
                this.entityql.insert(this.consumerSubscribe, consumerSubscribe).execute();
            }
        }
    }

    @Override
    public void updateAll(List<Consumer> consumers) {
        this.entityql.update(this.consumer, consumers).execute();

        // CONSUMER_PARAMETERの更新
        List<ConsumerParameter> insertParameterList = new ArrayList<>();
        List<ConsumerParameter> updateParameterList = new ArrayList<>();
        List<ConsumerParameter> deleteParameterList = new ArrayList<>();

        // CONSUMER_SUBSCRIBEの更新
        List<ConsumerSubscribe> insertSubscribeList = new ArrayList<>();
        List<ConsumerSubscribe> deleteSubscribeList = new ArrayList<>();

        for (Consumer consumer : consumers) {
            for (ConsumerParameter consumerParameter : consumer.getParameters().values()) {
                if (consumerParameter.isDelete()) {
                    deleteParameterList.add(consumerParameter);
                }
                if (consumerParameter.isUpdate()) {
                    updateParameterList.add(consumerParameter);
                }
                if (consumerParameter.isInsert()) {
                    insertParameterList.add(consumerParameter);
                }
            }
            for (ConsumerSubscribe consumerSubscribe : consumer.getSubscribes().values()) {
                if (consumerSubscribe.isDelete()) {
                    deleteSubscribeList.add(consumerSubscribe);
                }
                if (consumerSubscribe.isInsert()) {
                    insertSubscribeList.add(consumerSubscribe);
                }
            }
        }
        this.entityql.delete(this.consumerParameter, deleteParameterList).execute();
        this.entityql.update(this.consumerParameter, updateParameterList).execute();
        this.entityql.insert(this.consumerParameter, insertParameterList).execute();
        this.entityql.delete(this.consumerSubscribe, deleteSubscribeList).execute();
        this.entityql.insert(this.consumerSubscribe, insertSubscribeList).execute();
    }

    @Override
    public void remove(Consumer consumer) {
        this.entityql.delete(this.consumerParameter, consumer.getParameters().values().stream().toList()).execute();
        this.entityql.delete(this.consumerSubscribe, consumer.getSubscribes().values().stream().toList()).execute();
        this.entityql.delete(this.consumer, consumer).execute();
    }

    private EntityqlSelectStarting<Consumer> select() {
        return this.entityql.from(this.consumer)
                .leftJoin(consumerParameter, on -> on.eq(this.consumer.consumerId, this.consumerParameter.consumerId))
                .leftJoin(consumerSubscribe, on -> on.eq(this.consumer.consumerId, this.consumerSubscribe.consumerId))
                .associate(this.consumer, this.consumerParameter,
                        (consumer, parameter) -> consumer.getParameters().put(parameter.getParameterName(), parameter))
                .associate(this.consumer, this.consumerSubscribe,
                        (consumer, subscribe) -> consumer.getSubscribes().put(subscribe.getEndpointId(), subscribe));
    }

}
