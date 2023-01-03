package org.nortis.application.tenant;

/**
 * テナントを登録するコマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param name テナント名
 * @param userId ユーザID
 * @version 1.0.0
 */
public record TenantNameUpdateCommand(
		String tenantId,
		String name,
		String userId) {

}
