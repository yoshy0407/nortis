package org.nortis.consumer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link ConsumerManager}のデフォルト実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DefaultConsumerManager implements ConsumerManager {

    private final Map<String, MessageConsumer> consumers;

    /**
     * インスタンスを生成します
     * 
     * @param consumers コンシューマ
     */
    public DefaultConsumerManager(List<MessageConsumer> consumers) {
        // @fromatter:off
        this.consumers = consumers.stream()
                .collect(Collectors.toUnmodifiableMap(c -> c.consumerType().getCode(), d -> d));
        //@formatter:on
    }

    @Override
    public Optional<MessageConsumer> getMessageConsumer(String consumerTypeCode) {
        return Optional.ofNullable(this.consumers.get(consumerTypeCode));
    }

}
