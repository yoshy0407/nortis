package org.nortis.application.user.model;

import lombok.Builder;

/**
 * テナントモデル
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId テナントID
 * @param roleId   ロールID
 */
@Builder
public record TenantRoleModel(String tenantId, String roleId) {

}
