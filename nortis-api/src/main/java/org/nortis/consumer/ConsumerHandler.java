package org.nortis.consumer;

/**
 * コンシューマの処理インタフェース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface ConsumerHandler {

    /**
     * 処理成功時に呼び出されるメソッドです
     * 
     * @param messageConsumer    メッセージコンシューマ
     * @param consumerParameters コンシューマのパラメータ
     */
    void handleSuccess(MessageConsumer messageConsumer, ConsumerParameters consumerParameters);

    /**
     * 処理失敗時に呼び出されるメソッドです
     * 
     * @param messageConsumer    メッセージコンシューマ
     * @param consumerParameters コンシューマパラメータ
     * @param ex                 発生した例外
     */
    void handleFailure(MessageConsumer messageConsumer, ConsumerParameters consumerParameters,
            ConsumerFailureException ex);
}
