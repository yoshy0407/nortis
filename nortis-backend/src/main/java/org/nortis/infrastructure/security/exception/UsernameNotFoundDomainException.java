package org.nortis.infrastructure.security.exception;

import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCode;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * ユーザ名が存在しない場合の例外クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class UsernameNotFoundDomainException extends DomainException {

    public UsernameNotFoundDomainException(MessageCode messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    public UsernameNotFoundDomainException(String defaultMessage, String messageId, Object... args) {
        super(defaultMessage, messageId, args);
    }

    public UsernameNotFoundDomainException(String defaultMessage, String messageId, Throwable cause, Object... args) {
        super(defaultMessage, messageId, cause, args);
    }

    public UsernameNotFoundDomainException(MessageCode messageCode) {
        super(messageCode);
    }

    public UsernameNotFoundException convertSecurityException(MessageSource messageSource) {
        return new UsernameNotFoundException(resolveMessage(messageSource), getCause());
    }

}
