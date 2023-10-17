package org.nortis.test;

import java.util.List;
import lombok.Getter;
import org.nortis.consumer.ConsumerFailureException;
import org.nortis.consumer.ConsumerParameters;
import org.nortis.consumer.MessageConsumer;
import org.nortis.consumer.model.ConsumerType;
import org.nortis.consumer.model.Message;
import org.nortis.consumer.parameter.ParameterDefinition;

/**
 * モックコンシューマです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class MockMessageConsumer implements MessageConsumer {

    private final ConsumerType consumerType;

    private final List<ParameterDefinition<?>> consumerParameters;

    @Getter
    private Message message;

    @Getter
    private ConsumerParameters parameter;

    /**
     * コンストラクター
     * 
     * @param consumerType       コンシューマタイプ
     * @param consumerParameters コンシューマパラメータ
     */
    public MockMessageConsumer(ConsumerType consumerType, List<ParameterDefinition<?>> consumerParameters) {
        this.consumerType = consumerType;
        this.consumerParameters = consumerParameters;
    }

    @Override
    public ConsumerType consumerType() {
        return this.consumerType;
    }

    @Override
    public List<ParameterDefinition<?>> consumerParameters() {
        return this.consumerParameters;
    }

    @Override
    public void consume(Message message, ConsumerParameters parameter) throws ConsumerFailureException {
        this.message = message;
        this.parameter = parameter;
    }

    @Override
    public String consumerName() {
        return "mockConsumer";
    }

}
