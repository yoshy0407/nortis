package org.nortis.port.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.user.UserApplicationService;
import org.nortis.domain.tenant.event.TenantRoleDeletedEvent;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * ユーザのイベントリスナーです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class SuserEventListener {

    private final UserApplicationService userApplicationService;

    /**
     * テナントロール削除のイベントを受信します
     * 
     * @param event イベント
     */
    @EventListener
    public void subscribeTenantRoleDeleted(TenantRoleDeletedEvent event) {
        SecurityContextUtils.setAuthentication(event.getAuthentication());
        try {
            this.userApplicationService.revokeRoleFromRoleDeleted(event.getRoleId());
        } catch (Exception ex) {
            log.error("ユーザのテナントロール削除イベントの受信に失敗しました");
        }
    }

}
