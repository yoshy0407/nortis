package org.nortis.application.tenant.model;

import java.util.List;
import lombok.Builder;

/**
 * ロールの権限の更新コマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.1.1
 * @param tenantId            テナントID
 * @param roleId              ロールID
 * @param grantAuthorityList  追加する権限のリスト
 * @param revokeAuthorityList 削除する権限のリスト
 */
@Builder
public record RoleAuthorityUpdateCommand(String tenantId, String roleId, List<String> grantAuthorityList,
        List<String> revokeAuthorityList) {

}
