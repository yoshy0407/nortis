package org.nortis.port.rest;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.nortis.application.endpoint.EndpointReceiveApplicationService;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.port.rest.resource.ReceiveEventResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * エンドポイントの受信のコントローラです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RestController
public class EndpointReceiveRestController {

    private final EndpointReceiveApplicationService endpointReceiveApplicationService;

    /**
     * エンドポイントの受信処理を実行します
     * 
     * @param tenantIdentifier   テナント識別子
     * @param endpointIdentifier エンドポイント識別子
     * @param parameter          パラメーター
     * @param user               認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/notice/{tenantIdentifier}/{endpointIdentifier}")
    public void receiveEndpoint(@PathVariable String tenantIdentifier, @PathVariable String endpointIdentifier,
            @RequestBody Map<String, Object> parameter, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        this.endpointReceiveApplicationService.receive(tenantIdentifier, endpointIdentifier, parameter, user,
                translator());
    }

    private ApplicationTranslator<ReceiveEvent, ReceiveEventResource> translator() {
        return data -> {
            return new ReceiveEventResource(data.getEventId().toString(),
                    data.getOccuredOn().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        };
    }
}
