package org.nortis.application.endpoint;

/**
 * エンドポイントを削除するコマンド
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @version 1.0.0
 */
public record EndpointDeleteCommand(
		String tenantId,
		String endpointId) {

}
