package org.nortis.domain.consumer;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.infrastructure.doma.entity.ImmutableEntity;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 * コンシューマサブスクライバー
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "CONSUMER_SUBSCRIBE")
public class ConsumerSubscribe extends ImmutableEntity {

    @Id
    @Column(name = "CONSUMER_ID")
    private final ConsumerId consumerId;

    @Id
    @Column(name = "ENDPOINT_ID")
    private final EndpointId endpointId;

    /**
     * インスタンスを生成します
     * 
     * @param consumerId コンシューマID
     * @param endpointId エンドポイントID
     * @param createId   作成者ID
     * @param createDt   作成日付
     */
    public ConsumerSubscribe(ConsumerId consumerId, EndpointId endpointId, String createId, LocalDateTime createDt) {
        super(createId, createDt);
        this.consumerId = consumerId;
        this.endpointId = endpointId;
    }

    /**
     * 作成します
     * 
     * @param consumerId コンシューマID
     * @param endpointId エンドポイントID
     * @return 作成結果
     */
    public static ConsumerSubscribe create(ConsumerId consumerId, EndpointId endpointId) {
        return new ConsumerSubscribe(consumerId, endpointId, SecurityContextUtils.getCurrentAuthorizedId().toString(),
                LocalDateTime.now());
    }

}
