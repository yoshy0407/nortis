package org.nortis.domain.consumer;

/**
 * メッセージの送信に失敗した場合の例外です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class MessageFailureException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 例外を構築します
	 * @param message メッセージ
	 * @param cause 例外クラス
	 */
	public MessageFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 例外を構築します
	 * @param message メッセージ
	 */
	public MessageFailureException(String message) {
		super(message);
	}

}
