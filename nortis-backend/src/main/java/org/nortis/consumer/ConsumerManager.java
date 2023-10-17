package org.nortis.consumer;

import java.util.Optional;

/**
 * {@link MessageConsumer}を管理するクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface ConsumerManager {

    /**
     * メッセージコンシューマを取得します
     * 
     * @param consumerTypeCode コンシューマータイプのコード値
     * @return メッセージコンシューマー
     */
    public Optional<MessageConsumer> getMessageConsumer(String consumerTypeCode);

    /**
     * 成功時の処理を実行します
     * 
     * @param messageConsumer    メッセージコンシューマ
     * @param consumerParameters コンシューマのパラメータ
     */
    public void handleSuccess(MessageConsumer messageConsumer, ConsumerParameters consumerParameters);

    /**
     * 失敗時の処理を実行します
     * 
     * @param messageConsumer    メッセージコンシューマ
     * @param consumerParameters コンシューマのパラメータ
     * @param ex                 発生した例外
     */
    public void handleFailure(MessageConsumer messageConsumer, ConsumerParameters consumerParameters,
            ConsumerFailureException ex);

}
