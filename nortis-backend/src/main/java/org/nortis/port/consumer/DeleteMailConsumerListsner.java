package org.nortis.port.consumer;

import lombok.AllArgsConstructor;
import org.nortis.application.consumer.mail.MailConsumerApplicationService;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.springframework.stereotype.Component;

/**
 * メールの削除のイベントリスナー
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Component
public class DeleteMailConsumerListsner {

	/** メールコンシューマのアプリケーションサービス */
	private final MailConsumerApplicationService mailConsumerApplicationService;
	
	/**
	 * イベントを受信します
	 * @param event テナント削除イベント
	 */
	public void subscribe(TenantDeletedEvent event) {
		
	}
}
