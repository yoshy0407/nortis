package org.nortis.infrastructure.doma.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.event.ReceiveEventRepository;
import org.nortis.domain.event.ReceiveEvent_;
import org.nortis.domain.event.SendMessage;
import org.nortis.domain.event.SendMessage_;
import org.nortis.domain.event.value.EventId;
import org.nortis.domain.event.value.Subscribed;
import org.nortis.domain.tenant.value.TenantId;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStarting;
import org.springframework.stereotype.Repository;

/**
 * {@link ReceiveEventRepository}のDomaの実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Repository
public class DomaReceiveEventRepository implements ReceiveEventRepository {

    private final Entityql entityql;

    private final ReceiveEvent_ receiveEvent = new ReceiveEvent_();

    private final SendMessage_ sendMessage = new SendMessage_();

    @Override
    public Optional<ReceiveEvent> getByEventId(EventId eventId) {
        //@formatter:off
        return this.entityql.from(this.receiveEvent)
                .innerJoin(this.sendMessage, on -> on.eq(this.receiveEvent.eventId, this.sendMessage.eventId))
                .where(c -> c.eq(this.receiveEvent.eventId, eventId))
                .associate(this.receiveEvent, this.sendMessage, (r, s) -> r.getSendMessageList().add(s))
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public List<ReceiveEvent> notSubscribed() {
        //@formatter:off
        return select()
                .where(c -> c.eq(this.receiveEvent.subscribed, Subscribed.FALSE))
                .orderBy(orderBy -> orderBy.asc(this.receiveEvent.eventId))
                .fetch();
        //@formatter:on
    }

    @Override
    public List<ReceiveEvent> notSubscribedEndpoint(TenantId tenantId, EndpointId endpointId) {
        //@formatter:off
        return select()
                .where(c -> {
                    c.eq(this.receiveEvent.subscribed, Subscribed.FALSE);
                    c.eq(this.receiveEvent.tenantId, tenantId);
                    c.eq(this.receiveEvent.endpointId, endpointId);
                })
                .orderBy(orderBy -> {
                    orderBy.asc(this.receiveEvent.eventId);
                })
                .fetch();
        //@formatter:on
    }

    @Override
    public void save(ReceiveEvent receiveEvent) {
        this.entityql.insert(this.receiveEvent, receiveEvent).execute();
        this.entityql.insert(this.sendMessage, receiveEvent.getSendMessageList()).execute();
    }

    @Override
    public void update(ReceiveEvent receiveEvent) {
        // RECEIVE_EVENTの更新
        this.entityql.update(this.receiveEvent, receiveEvent).execute();

        // SEND_MESSAGEへの追加・削除
        for (SendMessage sendMessage : receiveEvent.getSendMessageList()) {
            if (sendMessage.isInsert()) {
                this.entityql.insert(this.sendMessage, sendMessage).execute();
            }
            if (sendMessage.isDelete()) {
                this.entityql.delete(this.sendMessage, sendMessage).execute();
            }
        }
    }

    @Override
    public void updateAll(List<ReceiveEvent> receiveEvents) {
        // RECEIVE_EVENTの更新
        this.entityql.update(this.receiveEvent, receiveEvents).execute();

        // SEND_MESSAGEへの追加・削除
        //@formatter:off
        List<SendMessage> sendMessageList = receiveEvents.stream()
                .flatMap(d -> d.getSendMessageList().stream())
                .toList();
        //@formatter:on
        for (SendMessage sendMessage : sendMessageList) {
            if (sendMessage.isInsert()) {
                this.entityql.insert(this.sendMessage, sendMessage).execute();
            }
            if (sendMessage.isDelete()) {
                this.entityql.delete(this.sendMessage, sendMessage).execute();
            }
        }
    }

    @Override
    public void remove(ReceiveEvent receiveEvent) {
        this.entityql.delete(this.sendMessage, receiveEvent.getSendMessageList()).execute();
        this.entityql.delete(this.receiveEvent, receiveEvent).execute();
    }

    private EntityqlSelectStarting<ReceiveEvent> select() {
        //@formatter:off
        return this.entityql.from(this.receiveEvent)
                .innerJoin(this.sendMessage, on -> on.eq(this.receiveEvent.eventId, this.sendMessage.eventId))
                .associate(this.receiveEvent, this.sendMessage, (r, s) -> r.getSendMessageList().add(s));
        //@formatter:on
    }

}
