package org.nortis.application.user.model;

/**
 * ユーザ名変更を行うコマンドです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param userId   ユーザID
 * @param usernane ユーザ名
 */
public record SuserChangeNameCommand(String userId, String usernane) {

}
