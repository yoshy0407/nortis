package org.nortis.application.endpoint.model;

import lombok.Builder;

/**
 * メッセージテンプレートの削除コマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId   テナントID
 * @param endpointId エンドポイントID
 * @param textType   テキストタイプ
 */
@Builder
public record EndpointDeleteMessageTemplateCommand(String tenantId, String endpointId, String textType) {

}
