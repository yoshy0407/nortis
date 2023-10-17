package org.nortis.application.user.model;

import java.util.List;
import lombok.Builder;

/**
 * テナントの権限を付与するコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId         ユーザID
 * @param tenantRoleList ロール
 */
@Builder
public record SuserGrantRoleCommand(String userId, List<TenantRoleModel> tenantRoleList) {

}
