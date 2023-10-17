package org.nortis.domain.tenant.event;

import lombok.Getter;
import lombok.ToString;
import org.nortis.infrastructure.event.AbstractEvent;
import org.springframework.security.core.Authentication;

/**
 * テナントロールが削除された時に発行されるイベントです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
public class TenantRoleDeletedEvent extends AbstractEvent {

    private final String tenantId;

    private final String roleId;

    /**
     * コンストラクター
     * 
     * @param tenantId       テナントID
     * @param roleId         ロールID
     * @param authentication 認証
     */
    public TenantRoleDeletedEvent(String tenantId, String roleId, Authentication authentication) {
        super(authentication);
        this.tenantId = tenantId;
        this.roleId = roleId;
    }

}
