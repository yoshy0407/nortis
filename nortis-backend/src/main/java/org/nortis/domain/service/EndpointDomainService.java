package org.nortis.domain.service;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.RenderedMessage;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.template.TemplateRender;

/**
 * エンドポイントに関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class EndpointDomainService {

    private final TemplateRender templateRender;

    private final ReceiveEventFactory receiveEventFactory;

    /**
     * エンドポイントの受信処理を行います
     * 
     * @param endpoint   エンドポイント
     * @param parameters パラメータ
     * @return 受信イベント
     * @throws DomainException ビジネスロジックエラー
     */
    public ReceiveEvent receiveEndpoint(Endpoint endpoint, Map<String, Object> parameters) throws DomainException {
        //@formatter:off
        List<RenderedMessage> sendMessageList = endpoint.getMessageTemplateList().stream()
                .filter(d -> !d.isDelete())
                .map(d -> d.renderMessage(templateRender, parameters))
                .toList();
        //@formatter:on
        return this.receiveEventFactory.createEvent(endpoint.getTenantId(), endpoint.getEndpointId(), parameters,
                sendMessageList);
    }
}
