package org.nortis.infrastructure.doma.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.endpoint.RenderedMessage;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.event.SendMessage;
import org.nortis.domain.event.value.EventId;
import org.nortis.domain.event.value.Subscribed;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
"/META-INF/ddl/dropReceiveEvent.sql",
"/ddl/createReceiveEvent.sql",
"/META-INF/data/domain/del_ins_receiveEvent.sql"
})
//@formatter:on
class DomaReceiveEventRepositoryTest extends RepositoryTestBase {

    @Autowired
    Entityql entityql;

    DomaReceiveEventRepository repository;

    @BeforeEach
    void setup() {
        this.repository = new DomaReceiveEventRepository(entityql);
    }

    @Test
    void testNotSubscribed() throws DomainException {
        List<ReceiveEvent> receiveEventList = this.repository.notSubscribed();

        assertThat(receiveEventList).hasSize(3);
        ReceiveEvent event1 = receiveEventList.get(0);
        assertThat(event1.getEventId()).isNotNull();
        assertThat(event1.getTenantId()).isEqualTo(TenantId.create("1000000001"));
        assertThat(event1.getEndpointId()).isEqualTo(EndpointId.create("2000000001"));
        assertThat(event1.getOccuredOn()).isNotNull();
        assertThat(event1.getSubscribed()).isEqualByComparingTo(Subscribed.FALSE);
        assertThat(event1.getParameterJson()).isEqualTo("{ \"name\": \"John\" }");

        List<SendMessage> sendMessageList1 = event1.getSendMessageList();
        assertThat(sendMessageList1).hasSize(2);
    }

    @Test
    void testNotSubscribedEndpoint() throws DomainException {
        List<ReceiveEvent> receiveEventList = this.repository.notSubscribedEndpoint(TenantId.create("1000000001"),
                EndpointId.create("2000000001"));
        assertThat(receiveEventList).hasSize(2);
    }

    @Test
    void testSave() throws DomainException {
        ReceiveEvent receiveEvent = ReceiveEvent.create(TenantId.create("1000000001"), EndpointId.create("2000000001"),
                null, Lists.list(new RenderedMessage(TextType.TEXT, "subject", "body")));

        this.repository.save(receiveEvent);

        Optional<ReceiveEvent> optEvent = this.repository.getByEventId(receiveEvent.getEventId());
        assertThat(optEvent).isPresent();
    }

    @Test
    void testUpdate() throws DomainException {
        EventId eventId = EventId.create("A07B2A86-35F1-4C3E-ADF3-D91D3AA36011");
        Optional<ReceiveEvent> event = this.repository.getByEventId(eventId);

        event.get().subscribe();

        this.repository.update(event.get());
    }

    @Test
    void testUpdateAll() {
        List<ReceiveEvent> receiveEventList = this.repository.notSubscribed();

        this.repository.updateAll(receiveEventList);
    }

    @Test
    void testRemove() throws DomainException {

        EventId eventId = EventId.create("A07B2A86-35F1-4C3E-ADF3-D91D3AA36011");
        Optional<ReceiveEvent> event = this.repository.getByEventId(eventId);
        this.repository.remove(event.get());

        Optional<ReceiveEvent> optEvent = this.repository.getByEventId(eventId);
        assertThat(optEvent).isEmpty();
    }

}
