package org.nortis.application.mail;

/**
 * メールの登録コマンド
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param mailAddress メールアドレス
 * @param userId ユーザID
 * @version 1.0.0
 */
public record MailRegisterCommand(
		String tenantId,
		String endpointId, 
		String mailAddress, 
		String userId) {

}
