package org.nortis.application.consumer.model;

import lombok.Builder;

/**
 * コンシューマーの削除コマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId   テナントID
 * @param consumerId コンシューマID
 */
@Builder
public record ConsumerDeleteCommand(String tenantId, String consumerId) {

}
