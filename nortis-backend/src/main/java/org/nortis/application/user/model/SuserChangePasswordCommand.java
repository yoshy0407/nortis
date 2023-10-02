package org.nortis.application.user.model;

import lombok.Builder;

/**
 * パスワード変更コマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId   ユーザID
 * @param password パスワード
 */
@Builder
public record SuserChangePasswordCommand(String userId, String password) {

}
