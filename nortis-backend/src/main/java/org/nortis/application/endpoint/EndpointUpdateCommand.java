package org.nortis.application.endpoint;

/**
 * エンドポイントのテンプレートの変更コマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param endpointName エンドポイント名
 * @param subjectTemplate サブジェクトテンプレート名
 * @param messageTemplate メッセージテンプレート
 * @param userId ユーザID
 * @version 1.0.0
 */
public record EndpointUpdateCommand(
		String tenantId,
		String endpointId,
		String endpointName,
		String subjectTemplate,
		String messageTemplate,
		String userId) {

}
