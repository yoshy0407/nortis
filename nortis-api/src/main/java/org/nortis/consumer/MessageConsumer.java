package org.nortis.consumer;

import java.util.List;
import org.nortis.consumer.model.ConsumerType;
import org.nortis.consumer.model.Message;
import org.nortis.consumer.parameter.ParameterDefinition;

/**
 * メッセージを消費するインタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface MessageConsumer {

    /**
     * メッセージコンシューマ名
     * 
     * @return メッセージコンシューマ名
     */
    String consumerName();

    /**
     * コンシューマのタイプを返却します
     * 
     * @return コンシューマのタイプ
     */
    ConsumerType consumerType();

    /**
     * このコンシューマで使用するパラメータのリストを返します
     * 
     * @return パラメータのリスト
     */
    List<ParameterDefinition<?>> consumerParameters();

    /**
     * メッセージを消費します
     * 
     * @param message   メッセージ
     * @param parameter パラメータ
     * @throws ConsumerFailureException 処理中のエラー
     */
    void consume(Message message, ConsumerParameters parameter) throws ConsumerFailureException;
}
