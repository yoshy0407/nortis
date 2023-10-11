package org.nortis.port.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.event.ReceiveEventApplicationService;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 受信イベントのイベントリスナー
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class ReceiveEventEventListsner {

    private final ReceiveEventApplicationService receiveEventApplicationService;

    /**
     * テナントの削除イベントを受信します
     * 
     * @param event テナント削除イベント
     */
    @EventListener
    public void subscribeEndpointDeleted(EndpointDeletedEvent event) {
        SecurityContextUtils.setAuthentication(event.getAuthentication());
        try {
            this.receiveEventApplicationService.subscribeByEndpoint(event.getTenantId().toString(),
                    event.getEndpointId().toString());
        } catch (Exception ex) {
            log.error("受信イベントのエンドポイント削除イベントの受信に失敗しました", ex);
        }
    }

}
