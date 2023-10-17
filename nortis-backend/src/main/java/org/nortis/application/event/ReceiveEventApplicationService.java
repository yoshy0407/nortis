package org.nortis.application.event;

import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.event.ReceiveEventRepository;
import org.nortis.domain.service.ConsumerDomainService;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 受信イベントのアプリケーションサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
@ApplicationService
public class ReceiveEventApplicationService {

    private final ReceiveEventRepository receiveEventRepository;

    private final ConsumerDomainService consumerDomainService;

    // :TODO イベントを見れるようにする

    /**
     * エンドポイントに対応する受信イベントを受信済みに変更する
     * 
     * @param rawTenantId   テナントID
     * @param rawEndpointId エンドポイントID
     * @throws DomainException ビジネスロジックエラー
     */
    public void subscribeByEndpoint(String rawTenantId, String rawEndpointId) throws DomainException {

        TenantId tenantId = TenantId.create(rawTenantId);
        EndpointId endpointId = EndpointId.create(rawEndpointId);
        List<ReceiveEvent> eventList = this.receiveEventRepository.notSubscribedEndpoint(tenantId, endpointId);

        for (ReceiveEvent event : eventList) {
            event.subscribe();
        }

        this.receiveEventRepository.updateAll(eventList);
    }

    /**
     * 受信イベントの消費処理を実行します
     * 
     * @throws DomainException ビジネスロジックエラー
     */
    public void consume() throws DomainException {
        List<ReceiveEvent> eventList = this.receiveEventRepository.notSubscribed();

        for (ReceiveEvent event : eventList) {
            this.consumerDomainService.consumeEvent(event);
        }
    }

}
