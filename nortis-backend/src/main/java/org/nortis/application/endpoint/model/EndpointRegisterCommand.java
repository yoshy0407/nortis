package org.nortis.application.endpoint.model;

import lombok.Builder;

/**
 * エンドポイント登録コマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantId           テナントID
 * @param endpointIdentifier エンドポイント識別子
 * @param endpointName       エンドポイント名
 * @param textType           テキストタイプ
 * @param subjectTemplate    サブジェクトテンプレート
 * @param bodyTemplate       テンプレートテンプレート
 * @version 1.0.0
 */
@Builder
public record EndpointRegisterCommand(String tenantId, String endpointIdentifier, String endpointName, String textType,
        String subjectTemplate, String bodyTemplate) {

}
