package org.nortis.domain.event;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.ConsumeFailureException;
import org.nortis.domain.consumer.Message;
import org.nortis.domain.consumer.MessageConsumer;
import org.nortis.domain.consumer.MessageFactory;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;

/**
 * 受信イベントに関するドメインサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class ReceiveEventConsumeDomainService {

	private final EndpointRepository endpointRepository;
	
	private final List<MessageConsumer> messageConsumers;

	private final MessageFactory messageFactory;
	
	
	/**
	 * 受信イベントを消費します
	 * @param event 受信イベント
	 * @throws ConsumeFailureException 内部のメッセージエラー
	 */
	public void consumeEvent(ReceiveEvent event) throws ConsumeFailureException {
		Optional<Endpoint> optEndpoint = 
				this.endpointRepository.get(event.getTenantId(), event.getEndpointId());
		
		if (optEndpoint.isEmpty()) {
			DomainException ex = new DomainException("MSG00003", "エンドポイント");
			throw new ConsumeFailureException("イベントの受信処理に失敗しました", ex);
		}
		
		Message message = this.messageFactory.createMessage(optEndpoint.get(), event);

		for (MessageConsumer messageConsumer : this.messageConsumers) {
			messageConsumer.consume(message);
		}
	}
	
}
