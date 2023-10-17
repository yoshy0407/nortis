package org.nortis.application.tenant.model;

import lombok.Builder;

/**
 * テナントのロール名を変更するコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId テナントID
 * @param roleId   ロールID
 * @param roleName ロール名
 */
@Builder
public record RoleNameChangeCommand(String tenantId, String roleId, String roleName) {

}
