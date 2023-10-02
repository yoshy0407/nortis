package org.nortis.domain.consumer;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;

/**
 * メールコンシューマのリポジトリです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface ConsumerRepository {

    /**
     * コンシューマIDからオブジェクトを取得します
     * 
     * @param tenantId   テナントID
     * @param consumerId コンシューマID
     * @return コンシューマ
     */
    Optional<Consumer> get(TenantId tenantId, ConsumerId consumerId);

    /**
     * エンドポイントIDからオブジェクトを取得します
     * 
     * @param endpointId エンドポイントID
     * @return コンシューマ
     */
    List<Consumer> getFromEndpoint(EndpointId endpointId);

    /**
     * コンシューマを保存します
     * 
     * @param consumer コンシューマ
     */
    void save(Consumer consumer);

    /**
     * コンシューマを更新します
     * 
     * @param consumer コンシューマ
     */
    void update(Consumer consumer);

    /**
     * 複数のコンシューマを更新します
     * 
     * @param consumers 複数のコンシューマ
     */
    void updateAll(List<Consumer> consumers);

    /**
     * コンシューマを削除します
     * 
     * @param consumer コンシューマ
     */
    void remove(Consumer consumer);

}
