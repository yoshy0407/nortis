package org.nortis.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.consumer.ConsumerManager;
import org.nortis.consumer.MessageConsumer;
import org.nortis.consumer.parameter.ParameterDefinition;
import org.nortis.consumer.parameter.ValidationResult;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.ConsumerParameterValidationException;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * コンシューマのドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@DomainService
@AllArgsConstructor
public class ConsumerDomainService {

    private final ConsumerManager consumerManager;

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
}
