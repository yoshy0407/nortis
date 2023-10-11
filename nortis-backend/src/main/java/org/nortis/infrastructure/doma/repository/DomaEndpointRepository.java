package org.nortis.infrastructure.doma.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.Endpoint_;
import org.nortis.domain.endpoint.MessageTemplate;
import org.nortis.domain.endpoint.MessageTemplate_;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.application.Paging;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStarting;
import org.springframework.stereotype.Repository;

/**
 * {@link EndpointRepository}のDomaの実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Repository
public class DomaEndpointRepository implements EndpointRepository {

    private final Entityql entityql;

    private final Endpoint_ endpoint = new Endpoint_();

    private final MessageTemplate_ messageTemplate = new MessageTemplate_();

    @Override
    public Optional<Endpoint> get(TenantId tenantId, EndpointId endpointId) {
        //@formatter:off
        return select()
                .where(where -> {
                    where.eq(endpoint.tenantId, tenantId);
                    where.eq(endpoint.endpointId, endpointId);
                })
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public Optional<Endpoint> getByEndpointIdentifier(TenantId tenantId, EndpointIdentifier endpointIdentifier) {
        //@formatter:off
        return select()
                .where(where -> {
                    where.eq(endpoint.tenantId, tenantId);
                    where.eq(endpoint.endpointIdentifier, endpointIdentifier);
                })
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public List<Endpoint> getFromTenantId(TenantId tenantId) {
        //@formatter:off
        return select()
                .where(where -> {
                    where.eq(endpoint.tenantId, tenantId);
                })
                .orderBy(orderby -> {
                    orderby.asc(this.endpoint.endpointId);
                })
                .fetch();
        //@formatter:on
    }

    @Override
    public List<Endpoint> getList(TenantId tenantId, Paging paging) {
        //@formatter:off
        return select()
                .where(c -> c.eq(endpoint.tenantId, tenantId))
                .orderBy(o -> o.asc(endpoint.endpointId))
                .offset(paging.offset())
                .limit(paging.limit())
                .fetch();
        //@formatter:on
    }

    @Override
    public void save(Endpoint endpoint) {
        entityql.insert(this.endpoint, endpoint).execute();
        entityql.insert(this.messageTemplate, endpoint.getMessageTemplateList()).execute();
    }

    @Override
    public void update(Endpoint endpoint) {
        this.entityql.update(this.endpoint, endpoint).execute();
        List<MessageTemplate> messageTemplateList = endpoint.getMessageTemplateList();
        for (MessageTemplate messageTemplate : messageTemplateList) {
            if (messageTemplate.isInsert()) {
                this.entityql.insert(this.messageTemplate, messageTemplate).execute();
            }
            if (messageTemplate.isUpdate()) {
                this.entityql.update(this.messageTemplate, messageTemplate).execute();
            }
            if (messageTemplate.isDelete()) {
                this.entityql.delete(this.messageTemplate, messageTemplate).execute();
            }
        }
    }

    @Override
    public void remove(Endpoint endpoint) {
        entityql.delete(this.messageTemplate, endpoint.getMessageTemplateList()).execute();
        entityql.delete(this.endpoint, endpoint).execute();
    }

    @Override
    public void removeAll(List<Endpoint> endpointList) {
        //@formatter:off
        List<MessageTemplate> messageTemplateList = endpointList.stream()
                .flatMap(d -> d.getMessageTemplateList().stream())
                .toList();                
        //@formatter:on
        entityql.delete(messageTemplate, messageTemplateList).execute();
        entityql.delete(endpoint, endpointList).execute();
    }

    private EntityqlSelectStarting<Endpoint> select() {
        //@formatter:off
        return entityql.from(endpoint)
                .innerJoin(messageTemplate, on -> on.eq(endpoint.endpointId, messageTemplate.endpointId))
                .associate(endpoint, messageTemplate, (e, m) -> e.getMessageTemplateList().add(m));
        //@formatter:on
    }

}
