package org.nortis.application.user;

/**
 * ユーザ名変更を行うコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId ユーザID
 * @param usernane ユーザ名
 * @param updateUserId 更新者ID
 */
public record SuserChangeNameCommand(
		String userId,
		String usernane,
		String updateUserId) {

}
