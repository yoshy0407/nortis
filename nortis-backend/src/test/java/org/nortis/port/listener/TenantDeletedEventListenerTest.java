package org.nortis.port.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.nortis.ListenerTestBase;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.user.TestUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

@SpringBootTest(classes = TenantDeletedEventListenerTestConfig.class)
class TenantDeletedEventListenerTest extends ListenerTestBase {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    EndpointApplicationService endpointApplicationService;

    @Test
    void testSubscribe() throws DomainException {
        TenantDeletedEvent event = new TenantDeletedEvent(TenantId.create("1000000001"),
                new PreAuthenticatedAuthenticationToken(TestUsers.memberUser().getUserDetails(), null));

        this.applicationEventPublisher.publishEvent(event);

        verify(this.endpointApplicationService).deleteFromTenantId(eq("1000000001"));
    }

    @Test
    void testSubscribeError() throws DomainException {
        Logger logger = (Logger) LoggerFactory.getLogger(TenantDeletedEventListener.class);
        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);

        logger.addAppender(mockAppender);

        doThrow(new RuntimeException()).when(this.endpointApplicationService).deleteFromTenantId(eq("1000000001"));

        TenantDeletedEvent event = new TenantDeletedEvent(TenantId.create("1000000001"),
                new PreAuthenticatedAuthenticationToken(TestUsers.memberUser().getUserDetails(), null));

        this.applicationEventPublisher.publishEvent(event);

        ArgumentCaptor<ILoggingEvent> eventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
        verify(mockAppender).doAppend(eventCaptor.capture());

        ILoggingEvent logingEvent = eventCaptor.getValue();
        assertThat(logingEvent.getMessage()).isEqualTo("テナント削除の受信処理に失敗しました");
    }

}
