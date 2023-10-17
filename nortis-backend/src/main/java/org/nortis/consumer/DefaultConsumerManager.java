package org.nortis.consumer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * {@link ConsumerManager}のデフォルト実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Component
public class DefaultConsumerManager implements ConsumerManager {

    private final Map<String, MessageConsumer> consumers;

    private final List<ConsumerHandler> consumerHandlers;

    /**
     * インスタンスを生成します
     * 
     * @param consumers        コンシューマ
     * @param consumerHandlers {@link ConsumerHandler}のリスト
     */
    public DefaultConsumerManager(List<MessageConsumer> consumers, List<ConsumerHandler> consumerHandlers) {
        // @fromatter:off
        this.consumers = consumers.stream()
                .collect(Collectors.toUnmodifiableMap(c -> c.consumerType().getCode(), d -> d));
        //@formatter:on
        this.consumerHandlers = consumerHandlers;
    }

    @Override
    public Optional<MessageConsumer> getMessageConsumer(String consumerTypeCode) {
        return Optional.ofNullable(this.consumers.get(consumerTypeCode));
    }

    @Override
    public void handleSuccess(MessageConsumer messageConsumer, ConsumerParameters consumerParameters) {
        this.consumerHandlers.forEach(handler -> handler.handleSuccess(messageConsumer, consumerParameters));
    }

    @Override
    public void handleFailure(MessageConsumer messageConsumer, ConsumerParameters consumerParameters,
            ConsumerFailureException ex) {
        this.consumerHandlers.forEach(handler -> handler.handleFailure(messageConsumer, consumerParameters, ex));
    }

}
