package org.nortis.application.consumer.model;

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
public record MailAddressDeleteCommand(
		String consumerId, 
		List<String> mailAddressList, 
		String userId) {

}
