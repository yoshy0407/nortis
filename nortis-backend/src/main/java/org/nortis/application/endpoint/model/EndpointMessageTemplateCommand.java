package org.nortis.application.endpoint.model;

import lombok.Builder;

/**
 * メッセージテンプレートの追加コマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId        テナントID
 * @param endpointId      エンドポイントID
 * @param textType        テキストタイプ
 * @param subjectTemplate サブジェクトテンプレート
 * @param bodyTemplate    ボディテンプレート
 */
@Builder
public record EndpointMessageTemplateCommand(String tenantId, String endpointId, String textType,
        String subjectTemplate, String bodyTemplate) {

}
