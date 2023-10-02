package org.nortis.infrastructure.security.exception;

import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCode;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.InsufficientAuthenticationException;

/**
 * 認可の結果権限がないことを表す例外です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class NoAuthorityDomainException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * 例外を構築します
     * 
     * @param messageCode メッセージコード
     * @param cause       例外
     */
    public NoAuthorityDomainException(MessageCode messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    /**
     * 例外を構築します
     * 
     * @param defaultMessage デフォルトのメッセージ
     * @param messageId      メッセージID
     * @param args           引数
     */
    public NoAuthorityDomainException(String defaultMessage, String messageId, Object... args) {
        super(defaultMessage, messageId, args);
    }

    /**
     * 例外を構築します
     * 
     * @param defaultMessage デフォルトのメッセージ
     * @param messageId      メッセージID
     * @param cause          例外
     * @param args           引数
     */
    public NoAuthorityDomainException(String defaultMessage, String messageId, Throwable cause, Object... args) {
        super(defaultMessage, messageId, cause, args);
    }

    /**
     * 例外を構築します
     * 
     * @param messageCode メッセージコード
     */
    public NoAuthorityDomainException(MessageCode messageCode) {
        super(messageCode);
    }

    /**
     * SpringSecurityの{@link InsufficientAuthenticationException}に変換します
     * 
     * @param messageSource {@link MessageSource}
     * @return 例外
     */
    public InsufficientAuthenticationException convertSecurityException(MessageSource messageSource) {
        return new InsufficientAuthenticationException(resolveMessage(messageSource), getCause());
    }

}
