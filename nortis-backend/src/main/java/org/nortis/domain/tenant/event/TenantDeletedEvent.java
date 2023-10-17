package org.nortis.domain.tenant.event;

import lombok.Getter;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.event.AbstractEvent;
import org.springframework.security.core.Authentication;

/**
 * テナントを削除したことを表すイベントです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
public class TenantDeletedEvent extends AbstractEvent {

    /** テナントID */
    private final TenantId tenantId;

    /**
     * インスタンスを生成します
     * 
     * @param tenantId       テナントID
     * @param authentication 認証
     */
    public TenantDeletedEvent(TenantId tenantId, Authentication authentication) {
        super(authentication);
        this.tenantId = tenantId;
    }

}
