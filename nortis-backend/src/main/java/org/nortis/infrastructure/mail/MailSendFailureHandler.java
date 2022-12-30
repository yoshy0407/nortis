package org.nortis.infrastructure.mail;

import org.springframework.mail.MailException;
import jakarta.mail.MessagingException;

/**
 * メールの送信エラーを処理するインタフェーです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface MailSendFailureHandler {

	/**
	 * 送信メッセージのエラー時の処理です
	 * @param exception 発生した例外クラス
	 */
	void handleMessageError(MessagingException exception);
	
	/**
	 * 送信時のエラーの処理です
	 * @param exception 発生した例外クラス
	 */
	void handleSendError(MailException exception);

}
