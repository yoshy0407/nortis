package org.nortis.application.endpoint;

/**
 * エンドポイント登録コマンドです
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param endpointName エンドポイント名
 * @param userId ユーザID
 * @version 1.0.0
 */
public record EndpointRegisterCommand(
		String tenantId,
		String endpointId,
		String endpointName,
		String userId) {

}
