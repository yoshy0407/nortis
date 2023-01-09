package org.nortis.domain.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.template.TemplateRender;

/**
 * {@link Message}のファクトリクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class MessageFactory {

	private final TemplateRender templateRender;
	
	private final ObjectMapper objectMapper;
	
	/**
	 * {@link Message}を構築します
	 * @param endpoint エンドポイント
	 * @param receiveEvent 受信イベント
	 * @return メッセージ
	 */
	public Message createMessage(Endpoint endpoint, ReceiveEvent receiveEvent) {
		Map<String, Object> json = null;
		try {
			json = this.objectMapper.readValue(
					receiveEvent.getTemplateParameter(), 
					new TypeReference<Map<String, Object>>(){});
		} catch (JsonProcessingException e) {
			throw new UnexpectedException("MSG40001");
		}
		
		String subject = this.templateRender.render(
				receiveEvent.getEndpointId().toString(), 
				endpoint.getSubjectTemplate(), 
				json);
		
		String message = this.templateRender.render(
				receiveEvent.getEndpointId().toString(), 
				endpoint.getMessageTemplate(), json);
				
		return new Message(receiveEvent.getTenantId(), receiveEvent.getEndpointId(), subject, message);
	}
}
