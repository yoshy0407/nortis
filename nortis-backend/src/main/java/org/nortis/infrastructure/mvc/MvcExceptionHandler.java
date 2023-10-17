package org.nortis.infrastructure.mvc;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.infrastructure.exception.ConsumerParameterValidationException;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.message.MessageCode;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.exception.NoAuthorityDomainException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * エラーを処理してエラーレスポンスを返すクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Slf4j
@ControllerAdvice
public class MvcExceptionHandler {

    private final MessageSource messageSource;

    /**
     * {@link DomainException}を処理して、エラーレスポンスを返却します
     * 
     * @param ex 例外
     * @return エラーレスポンス
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public ErrorResponse handleDomainException(DomainException ex) {
        return new ErrorResponse(LocalDateTime.now(), ex.getMessageId(), ex.resolveMessage(this.messageSource));
    }

    /**
     * {@link ConsumerParameterValidationException}を処理して、エラーレスポンスを返却します
     * 
     * @param ex 例外
     * @return エラーレスポンス
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConsumerParameterValidationException.class)
    public ErrorResponse handleDomainException(ConsumerParameterValidationException ex) {
        // 渡されているメッセージを解決する必要があるので、こっちを使用するr
        return new ErrorResponse(LocalDateTime.now(), ex.getMessageId(), ex.getMessage());
    }

    /**
     * {@link NoAuthorityDomainException}を処理して、エラーレスポンスを返却します
     * 
     * @param ex 例外
     * @return エラーレスポンス
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAuthorityDomainException.class)
    public ErrorResponse handleNoAuthorityDomainException(NoAuthorityDomainException ex) {
        return handleDomainException(ex);
    }

    /**
     * {@link UnexpectedException}を処理して、エラーレスポンスを返却します
     * 
     * @param ex 例外
     * @return エラーレスポンス
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UnexpectedException.class)
    public ErrorResponse handleUnexpectedException(UnexpectedException ex) {
        log.error("unexpected Error: %s".formatted(ex.resolveLogFormatMessage(messageSource)), ex);
        return create500Response();
    }

    /**
     * 上記意外の例外が投げられた際に処理してエラーレスポンスを返却します
     * 
     * @param ex 例外
     * @return エラーレスポンス
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleException(Throwable ex) {
        log.error("unexpected Error", ex);
        return create500Response();
    }

    private ErrorResponse create500Response() {
        MessageCode code = MessageCodes.nortis00500();
        String message = code.resolveMessage(messageSource, LocaleContextHolder.getLocale());
        return new ErrorResponse(LocalDateTime.now(), code.getCode(), message);
    }
}
