package org.nortis.port.endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * テナント削除の受信クラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class TenantDeletedEventListener {

	private final EndpointApplicationService endpointApplicationService;
	
	/**
	 * ドメインイベントを受信します
	 * @param event テナント削除イベント
	 */
	@EventListener
	public void subscribe(TenantDeletedEvent event) {
		try {
			this.endpointApplicationService.deleteFromTenantId(event.getTenantId().toString(), event.getUpdateUserId());
		} catch (Exception ex) {
			log.error("テナント削除の受信処理に失敗しました", ex);
		}
	}
}
