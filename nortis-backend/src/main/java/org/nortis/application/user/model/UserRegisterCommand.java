package org.nortis.application.user.model;

import java.util.List;
import lombok.Builder;

/**
 * ユーザ登録のコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param username       ユーザ名
 * @param loginId        ログインID
 * @param password       パスワード
 * @param adminFlg       管理者フラグ
 * @param tenantRoleList ロール情報
 */
@Builder
public record UserRegisterCommand(String username, String loginId, String password, Integer adminFlg,
        List<TenantRoleModel> tenantRoleList) {

}
