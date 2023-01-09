package org.nortis.application.endpoint;

/**
 * エンドポイントを削除するコマンド
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param userId 更新者ID
 * @version 1.0.0
 */
public record EndpointDeleteCommand(
		String tenantId,
		String endpointId,
		String userId) {

}
