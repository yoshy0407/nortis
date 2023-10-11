package org.nortis.port.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * テナント削除の受信クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class EndpointEventListener {

    private final EndpointApplicationService endpointApplicationService;

    /**
     * テナントの削除イベントを受信します
     * 
     * @param event テナント削除イベント
     */
    @EventListener
    public void subscribeTenantDeleted(TenantDeletedEvent event) {
        SecurityContextUtils.setAuthentication(event.getAuthentication());
        try {
            this.endpointApplicationService.deleteFromTenantId(event.getTenantId().toString());
        } catch (Exception ex) {
            log.error("エンドポイントのテナント削除イベントの受信に失敗しました", ex);
        }
    }

}
