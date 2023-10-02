package org.nortis.application.event;

import lombok.AllArgsConstructor;
import org.nortis.domain.event.ReceiveEventRepository;
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
@Transactional
@ApplicationService
public class ReceiveEventApplicationService {

    private final ReceiveEventRepository receiveEventRepository;

    // :TODO イベントを見れるようにする

    /**
     * エンドポイントに対応する受信イベントを受信済みに変更する
     * 
     * @param tenantId   テナントID
     * @param endpointId エンドポイントID
     * @throws DomainException ビジネスロジックエラー
     */
    public void subscribeByEndpoint(String tenantId, String endpointId) throws DomainException {

    }

    /**
     * 受信イベントの消費処理を実行します
     */
    public void consume() {

    }

}
