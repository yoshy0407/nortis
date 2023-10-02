package org.nortis.application.consumer.model;

import java.util.Map;
import lombok.Builder;

/**
 * メールの登録コマンド
 * 
 * @author yoshiokahiroshi
 * @param tenantId     テナントID
 * @param consumerId   コンシューマID
 * @param consumerName コンシューマ名
 * @param consumerType コンシューマタイプ
 * @param textType     テキストタイプ
 * @param parameter    パラメータ
 * @version 1.0.0
 */
@Builder
public record ConsumerUpdateCommand(String tenantId, String consumerId, String consumerName, String consumerType,
        String textType, Map<String, String> parameter) {

}
