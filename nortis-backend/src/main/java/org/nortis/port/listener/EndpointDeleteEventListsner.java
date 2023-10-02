package org.nortis.port.listener;

import lombok.AllArgsConstructor;
import org.nortis.application.consumer.ConsumerApplicationService;
import org.nortis.application.event.ReceiveEventApplicationService;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.springframework.stereotype.Component;

/**
 * エンドポイントの削除のイベントリスナー
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Component
public class EndpointDeleteEventListsner extends AbstractEventListener<EndpointDeletedEvent> {

    private final ConsumerApplicationService consumerApplicationService;

    private final ReceiveEventApplicationService receiveEventApplicationService;

    @Override
    protected void doSubscribe(EndpointDeletedEvent event) throws Exception {
        this.consumerApplicationService.unsubscribeEndpoint(event.getEndpointId().toString());
        this.receiveEventApplicationService.subscribeByEndpoint(event.getTenantId().toString(),
                event.getEndpointId().toString());
    }

    @Override
    protected String getProcessName() {
        return "エンドポイント削除イベントの受信";
    }
}
