package org.nortis.port.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.consumer.ConsumerApplicationService;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * コンシューマのイベントリスナー
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class ConsumerEventListsner {

    private final ConsumerApplicationService consumerApplicationService;

    /**
     * テナントの削除イベントを受信します
     * 
     * @param event テナント削除イベント
     */
    @EventListener
    public void subscribeEndpointDeleted(EndpointDeletedEvent event) {
        SecurityContextUtils.setAuthentication(event.getAuthentication());
        try {
            this.consumerApplicationService.unsubscribeEndpoint(event.getEndpointId().toString());
        } catch (Exception ex) {
            log.error("コンシューマのエンドポイント削除イベントの受信に失敗しました", ex);
        }
    }

}
