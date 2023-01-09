package org.nortis.port.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.event.ReceiveEventApplicationService;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * エンドポイント削除時の処理を行うサブスクライバーです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class EndpointDeletedEventSubscriber {

	private final ReceiveEventApplicationService receiveEventApplicationService;
	
	/**
	 * ドメインイベント受信します
	 * @param event イベント
	 */
	@EventListener
	public void subscribe(EndpointDeletedEvent event) {
		try {
			this.receiveEventApplicationService.subscribedByEndpointDeleted(event.getEndpointId().toString());
		} catch (Exception ex) {
			log.error("エンドポイント削除イベントの受信処理に失敗しました", ex);
		}
	}
}
