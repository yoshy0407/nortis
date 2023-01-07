package org.nortis.application.consumer.mail;

import java.util.List;

/**
 * メールの登録コマンド
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param mailAddressList 登録するメールアドレスたち
 * @param userId ユーザID
 * @version 1.0.0
 */
public record MailRegisterCommand(
		String tenantId,
		String endpointId, 
		List<String> mailAddressList, 
		String userId) {

}
