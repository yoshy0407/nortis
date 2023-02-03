package org.nortis.port.tenant;

/**
 * テナント作成リクエスト
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId テナントID
 * @param tenantName テナント名
 */
public record TenantRequest(
		String tenantId,
		String tenantName) {

}
