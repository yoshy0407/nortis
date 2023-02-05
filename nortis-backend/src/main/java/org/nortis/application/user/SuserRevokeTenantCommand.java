package org.nortis.application.user;

/**
 * テナントの権限を剥奪するコマンドです
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId ユーザID
 * @param tenantId テナントID
 * @param updateUserId 更新者ID
 */
public record SuserRevokeTenantCommand(
		String userId, 
		String tenantId, 
		String updateUserId) {

}