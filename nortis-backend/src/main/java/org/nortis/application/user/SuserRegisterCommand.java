package org.nortis.application.user;

/**
 * ユーザ登録のコマンドです
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId ユーザID
 * @param username ユーザ名
 * @param password パスワード
 * @param createUserId 作成者ID
 */
public record SuserRegisterCommand(
		String userId,
		String username,
		String password,
		String createUserId) {

}
