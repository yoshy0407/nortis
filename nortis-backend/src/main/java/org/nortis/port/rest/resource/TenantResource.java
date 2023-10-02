package org.nortis.port.rest.resource;

/**
 * テナントリソース
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId テナントID
 * @param tenantName テナント名
 */
public record TenantResource(
		String tenantId,
		String tenantName) {

}
