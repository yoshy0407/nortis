package org.nortis.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.RenderedMessage;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * 受信イベントのに関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class ReceiveEventFactory {

    private final ObjectMapper objectMapper;

    /**
     * 受信イベントを作成します
     * 
     * @param tenantId        テナントID
     * @param endpointId      エンドポイントID
     * @param parameter       パラメータ
     * @param sendMessageList 送信メッセージ
     * @return 受信イベント
     * @throws DomainException ビジネスロジックエラー
     */
    public ReceiveEvent createEvent(TenantId tenantId, EndpointId endpointId, Map<String, Object> parameter,
            List<RenderedMessage> sendMessageList) throws DomainException {
        String parameterJson = null;
        try {
            parameterJson = this.objectMapper.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(MessageCodes.nortis40002(), e);
        }
        return ReceiveEvent.create(tenantId, endpointId, parameterJson, sendMessageList);
    }

}
