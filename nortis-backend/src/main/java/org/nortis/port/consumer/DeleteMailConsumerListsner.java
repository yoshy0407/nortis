package org.nortis.port.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.consumer.mail.MailConsumerApplicationService;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * メールの削除のイベントリスナー
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class DeleteMailConsumerListsner {

	/** メールコンシューマのアプリケーションサービス */
	private final MailConsumerApplicationService mailConsumerApplicationService;
	
	/**
	 * イベントを受信します
	 * @param event テナント削除イベント
	 */
	@EventListener
	public void subscribe(EndpointDeletedEvent event) {
		try {
			this.mailConsumerApplicationService.removeEndpointIdByEndpointDeleted(
					event.getTenantId().toString(), 
					event.getUpdateUserId());
		} catch (Exception ex) {
			log.error("エンドポイント削除イベントの受信に失敗しました", ex);
		}
	}
}
