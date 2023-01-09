package org.nortis.domain.consumer;

/**
 * メッセージの送信に失敗した場合の例外です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ConsumeFailureException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 例外を構築します
	 * @param message メッセージ
	 * @param cause 例外クラス
	 */
	public ConsumeFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 例外を構築します
	 * @param message メッセージ
	 */
	public ConsumeFailureException(String message) {
		super(message);
	}

}
