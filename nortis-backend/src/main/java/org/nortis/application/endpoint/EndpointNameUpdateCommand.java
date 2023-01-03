package org.nortis.application.endpoint;

/**
 * エンドポイントの更新コマンドです
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param endpointName 更新するエンドポイント名
 * @param userId ユーザID
 * @version 1.0.0
 */
public record EndpointNameUpdateCommand(
		String tenantId,
		String endpointId,
		String endpointName,
		String userId) {

}
