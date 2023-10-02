package org.nortis.infrastructure.exception;

import org.nortis.domain.consumer.ConsumerParameter;
import org.nortis.infrastructure.message.MessageCode;

/**
 * {@link ConsumerParameter}のバリデーションエラーを表す例外です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ConsumerParameterValidationException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * コンストラクター
     * 
     * @param messageCode メッセージコード
     * @param cause       例外
     */
    public ConsumerParameterValidationException(MessageCode messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    /**
     * コンストラクター
     * 
     * @param messageCode メッセージコード
     */
    public ConsumerParameterValidationException(MessageCode messageCode) {
        super(messageCode);
    }

}
