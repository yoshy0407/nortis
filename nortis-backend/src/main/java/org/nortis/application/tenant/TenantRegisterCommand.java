package org.nortis.application.tenant;

import org.nortis.infrastructure.security.user.NortisUserDetails;

/**
 * テナントを登録するコマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param name テナント名
 * @param user ユーザ
 * @version 1.0.0
 */
public record TenantRegisterCommand(
		String tenantId,
		String name,
		NortisUserDetails user) {

}
