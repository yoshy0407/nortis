package org.nortis.port.listener;

import lombok.AllArgsConstructor;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.springframework.stereotype.Component;

/**
 * テナント削除の受信クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Component
public class TenantDeletedEventListener extends AbstractEventListener<TenantDeletedEvent> {

    private final EndpointApplicationService endpointApplicationService;

    @Override
    protected void doSubscribe(TenantDeletedEvent event) throws Exception {
        this.endpointApplicationService.deleteFromTenantId(event.getTenantId().toString());
    }

    @Override
    protected String getProcessName() {
        return "テナント削除イベント";
    }
}
