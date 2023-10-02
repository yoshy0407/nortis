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

}
