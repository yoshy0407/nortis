package org.nortis.domain.event;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.value.EventId;

/**
 * 受信イベントのリポジトリ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface ReceiveEventRepository {

    /**
     * イベントIDから取得します
     * 
     * @param eventId イベントID
     * @return 受信イベント
     */
    Optional<ReceiveEvent> getByEventId(EventId eventId);

    /**
     * 未受信のイベントを取得します
     * 
     * @return 未受信イベントのリスト
     */
    List<ReceiveEvent> notSubscribed();

    /**
     * 未受信の指定エンドポイントのイベントを取得します
     * 
     * @param endpointId エンドポイントID
     * @return 未受信イベントのリスト
     */
    List<ReceiveEvent> notSubscribedEndpoint(EndpointId endpointId);

    /**
     * 保存します
     * 
     * @param receiveEvent 受信イベント
     */
    void save(ReceiveEvent receiveEvent);

    /**
     * 更新します
     * 
     * @param receiveEvent 受信イベント
     */
    void update(ReceiveEvent receiveEvent);

    /**
     * 複数レコードを更新します
     * 
     * @param receiveEvents 受信イベントのリスト
     */
    void updateAll(List<ReceiveEvent> receiveEvents);

    /**
     * 削除します
     * 
     * @param receiveEvent 受信イベント
     */
    void remove(ReceiveEvent receiveEvent);

}
