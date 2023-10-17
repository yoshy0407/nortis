package org.nortis.application.user.model;

import java.util.List;
import lombok.Builder;

/**
 * テナントの権限を剥奪するコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId         ユーザID
 * @param tenantRoleList ロールのリスト
 */
@Builder
public record SuserRevokeRoleCommand(String userId, List<TenantRoleModel> tenantRoleList) {

}
