package org.nortis.application.event;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.domain.consumer.ConsumeFailureException;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.event.ReceiveEventCheckDomainService;
import org.nortis.domain.event.ReceiveEventConsumeDomainService;
import org.nortis.domain.event.ReceiveEventRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;


/**
 * 受信イベントに関するアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@ApplicationService
public class ReceiveEventApplicationService {

	/** 受信イベントのリポジトリ */
	private final ReceiveEventRepository receiveEventRepository;
	
	/** 受信イベントのチェックに関するドメインサービス */
	private final ReceiveEventCheckDomainService checkDomainService;
	
	/** 受信イベントの消費に関するドメインサービス */
	private final ReceiveEventConsumeDomainService consumeDomainService;
	
	/**
	 * 受信イベントを登録します
	 * @param <R> 結果クラス
	 * @param command 登録コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R register(
			ReceiveEventRegisterCommand command,
			ApplicationTranslator<ReceiveEvent, R> translator) throws DomainException {
		TenantId tenantId = TenantId.create(command.tenantId());
		EndpointId endpointId = EndpointId.create(command.endpointId());
		
		this.checkDomainService.checkBeforeRegister(tenantId, endpointId);
		
		ReceiveEvent receiveEvent = ReceiveEvent.create(tenantId, endpointId, command.paramaterJson());
		
		this.receiveEventRepository.save(receiveEvent);
		
		return translator.translate(receiveEvent);
	}
	
	/**
	 * イベント受信を実行します
	 */
	public void subscribeEvent() {
		List<ReceiveEvent> receiveEvents = this.receiveEventRepository.notSubscribed();
		for (ReceiveEvent receiveEvent : receiveEvents) {
			try {
				consumeDomainService.consumeEvent(receiveEvent);
			} catch (ConsumeFailureException e) {
				log.error(null);
			}
			receiveEvent.subscribe();
			this.receiveEventRepository.update(receiveEvent);
		}
	}
	
	/**
	 * 削除されたエンドポイントのイベントを受信済みに設定します
	 * @param endpointId エンドポイントID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void subscribedByEndpointDeleted(
			String endpointId) throws DomainException {
		EndpointId id = EndpointId.create(endpointId);
		List<ReceiveEvent> events = this.receiveEventRepository.notSubscribedEndpoint(id);
		if (events.isEmpty()) {
			return;
		}
		events.forEach(event -> event.subscribe());
		this.receiveEventRepository.updateAll(events);
	}
}
