package org.nortis.infrastructure.doma.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.Endpoint_;
import org.nortis.domain.endpoint.MessageTemplate;
import org.nortis.domain.endpoint.MessageTemplate_;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.tenant.value.TenantId;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.declaration.OrderByNameDeclaration;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
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
        return selectInternal(where -> {
            where.eq(endpoint.tenantId, tenantId);
            where.eq(endpoint.endpointId, endpointId);
        });
    }

    @Override
    public Optional<Endpoint> getByEndpointIdentifier(TenantId tenantId, EndpointIdentifier endpointIdentifier) {
        return selectInternal(where -> {
            where.eq(endpoint.tenantId, tenantId);
            where.eq(endpoint.endpointIdentifier, endpointIdentifier);
        });
    }

    @Override
    public List<Endpoint> getFromTenantId(TenantId tenantId) {
        return selectListInternal(where -> {
            where.eq(endpoint.tenantId, tenantId);
        }, orderby -> {
            orderby.asc(this.endpoint.endpointId);
        });
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

    private Optional<Endpoint> selectInternal(Consumer<WhereDeclaration> where) {
        //@formatter:off
        return entityql.from(endpoint)
                .innerJoin(messageTemplate, on -> on.eq(endpoint.endpointId, messageTemplate.endpointId))
                .where(where)
                .associate(endpoint, messageTemplate, (e, m) -> e.getMessageTemplateList().add(m))
                .fetchOptional();
        //@formatter:on
    }

    private List<Endpoint> selectListInternal(Consumer<WhereDeclaration> where,
            Consumer<OrderByNameDeclaration> orderBy) {
        //@formatter:off
        return entityql.from(endpoint)
                .innerJoin(messageTemplate, on -> on.eq(endpoint.endpointId, messageTemplate.endpointId))
                .where(where)
                .orderBy(orderBy)
                .associate(endpoint, messageTemplate, (e, m) -> e.getMessageTemplateList().add(m))
                .fetch();
        //@formatter:on
    }
}
