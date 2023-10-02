package org.nortis.application.tenant.model;

import java.util.List;
import lombok.Builder;

/**
 * テナントのロールを作成するコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId     テナントID
 * @param roleName     ロール名
 * @param authorityIds 設定する権限IDのリスト
 */
@Builder
public record RoleCreateCommand(String tenantId, String roleName, List<String> authorityIds) {

}
