package org.nortis.domain.consumer;

/**
 * メッセージを処理するコンシューマインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface MessageConsumer {

	/**
	 * 受信イベントを消費します
	 * @param message 送信メッセージ
	 * @throws ConsumeFailureException 受信処理に失敗した場合
	 */
	void consume(Message message) throws ConsumeFailureException;
	
}
