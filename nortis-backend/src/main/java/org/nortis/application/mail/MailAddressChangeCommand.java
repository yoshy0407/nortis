package org.nortis.application.mail;

/**
 * メールアドレスの変更コマンド
 * 
 * @author yoshiokahiroshi
 * @param consumerId コンシューマID
 * @param mailAddress メールアドレス
 * @param userId ユーザID
 * @version 1.0.0
 */
public record MailAddressChangeCommand(
		String consumerId, 
		String mailAddress, 
		String userId) {

}
