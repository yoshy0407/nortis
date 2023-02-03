package org.nortis.application.user;

/**
 * パスワード変更コマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId ユーザID
 * @param password パスワード
 * @param updateUserId 更新者ID
 */
public record SuserChangePasswordCommand(
		String userId,
		String password,
		String updateUserId) {

}
