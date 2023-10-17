package org.nortis.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.consumer.ConsumerFailureException;
import org.nortis.consumer.ConsumerManager;
import org.nortis.consumer.ConsumerParameters;
import org.nortis.consumer.MessageConsumer;
import org.nortis.consumer.parameter.ParameterDefinition;
import org.nortis.consumer.parameter.ValidationResult;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.ConsumerRepository;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.ConsumerParameterValidationException;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * コンシューマのドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@DomainService
@AllArgsConstructor
public class ConsumerDomainService {

    private final ConsumerManager consumerManager;

    private final ConsumerRepository consumerRepository;

    /**
     * コンシューマパラメータをチェックします
     * 
     * @param consumerType コンシューマタイプ
     * @param map          パラメータ
     * @throws DomainException チェックエラー
     */
    public void validateConsumerParameter(String consumerType, Map<String, String> map) throws DomainException {

        Optional<MessageConsumer> optMessageConsumer = this.consumerManager.getMessageConsumer(consumerType);
        if (optMessageConsumer.isEmpty()) {
            throw new DomainException(MessageCodes.nortis30005());
        }

        MessageConsumer messageConsumer = optMessageConsumer.get();
        List<ParameterDefinition<?>> parameterList = messageConsumer.consumerParameters();

        for (ParameterDefinition<?> parameter : parameterList) {
            String value = map.get(parameter.getParameterName());
            if (value == null) {
                // 必須なのに、パラメータとして存在しない
                if (parameter.isRequire()) {
                    throw new DomainException(MessageCodes.nortis30003(parameter.getDisplayName()));
                }
                continue;
            }
            ValidationResult result = parameter.validate(value);
            if (!result.isSuccess()) {
                throw new ConsumerParameterValidationException(MessageCodes.nortis30004(result.getMessage().get()));
            }
        }
    }

    /**
     * イベントを受信します
     * 
     * @param receiveEvent 受信イベント
     * @throws DomainException ビジネスロジックエラー
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void consumeEvent(ReceiveEvent receiveEvent) throws DomainException {
        List<Consumer> consumerList = this.consumerRepository.getFromEndpoint(receiveEvent.getEndpointId());

        for (Consumer consumer : consumerList) {
            Optional<MessageConsumer> optMessageConsumer = this.consumerManager
                    .getMessageConsumer(consumer.getConsumerTypeCode());

            if (optMessageConsumer.isEmpty()) {
                log.warn("対応するコンシューマが存在しません");
                continue;
            }
            MessageConsumer messageConsumer = optMessageConsumer.get();
            ConsumerParameters consumerParameters = consumer.getConsumerParameters();
            try {
                messageConsumer.consume(consumer.consumeForSend(receiveEvent), consumerParameters);
                this.consumerManager.handleSuccess(messageConsumer, consumerParameters);
            } catch (ConsumerFailureException e) {
                this.consumerManager.handleFailure(messageConsumer, consumerParameters, e);
            } catch (DomainException e) {
                throw e;
            }
        }
    }
}
