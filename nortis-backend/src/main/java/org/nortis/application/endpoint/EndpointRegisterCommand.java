package org.nortis.application.endpoint;

/**
 * エンドポイント登録コマンドです
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param endpointName エンドポイント名
 * @param subjectTemplate サブジェクトテンプレート
 * @param messageTemplate テンプレートテンプレート
 * @param userId ユーザID
 * @version 1.0.0
 */
public record EndpointRegisterCommand(
		String tenantId,
		String endpointId,
		String endpointName,
		String subjectTemplate,
		String messageTemplate,
		String userId) {

}
