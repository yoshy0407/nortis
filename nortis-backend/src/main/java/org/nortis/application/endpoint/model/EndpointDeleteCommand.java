package org.nortis.application.endpoint.model;

import lombok.Builder;

/**
 * エンドポイントを削除するコマンド
 * 
 * @author yoshiokahiroshi
 * @param tenantId   テナントID
 * @param endpointId エンドポイントID
 * @version 1.0.0
 */
@Builder
public record EndpointDeleteCommand(String tenantId, String endpointId) {

}
