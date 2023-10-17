package org.nortis.application.consumer.model;

import lombok.Builder;

/**
 * コンシューマのサブスクライブコマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId   テナントID
 * @param consumerId コンシューマID
 * @param endpointId エンドポイントID
 */
@Builder
public record ConsumerSubscribeCommand(String tenantId, String consumerId, String endpointId) {

}
