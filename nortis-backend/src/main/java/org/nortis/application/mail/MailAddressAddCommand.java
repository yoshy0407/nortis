package org.nortis.application.mail;

import java.util.List;

/**
 * メールアドレスの変更コマンド
 * 
 * @author yoshiokahiroshi
 * @param consumerId コンシューマID
 * @param mailAddressList メールアドレス
 * @param userId ユーザID
 * @version 1.0.0
 */
public record MailAddressAddCommand(
		String consumerId, 
		List<String> mailAddressList, 
		String userId) {

}
