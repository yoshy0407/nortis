package org.nortis.application.endpoint.model;

import lombok.Builder;

/**
 * エンドポイントのテンプレートの変更コマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantId     テナントID
 * @param endpointId   エンドポイントID
 * @param endpointName エンドポイント名
 * @version 1.0.0
 */
@Builder
public record EndpointNameUpdateCommand(String tenantId, String endpointId, String endpointName) {

}
