package org.nortis.domain.endpoint.event;

import lombok.Getter;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.event.AbstractEvent;
import org.springframework.security.core.Authentication;

/**
 * エンドポイントが削除されたことを表すイベントです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
public class EndpointDeletedEvent extends AbstractEvent {

    /** テナントID */
    private final TenantId tenantId;

    /** エンドポイントID */
    private final EndpointId endpointId;

    /**
     * インスタンスを生成します
     * 
     * @param tenantId       テナントID
     * @param endpointId     エンドポイントID
     * @param authentication 認証
     */
    public EndpointDeletedEvent(TenantId tenantId, EndpointId endpointId, Authentication authentication) {
        super(authentication);
        this.tenantId = tenantId;
        this.endpointId = endpointId;
    }

}
