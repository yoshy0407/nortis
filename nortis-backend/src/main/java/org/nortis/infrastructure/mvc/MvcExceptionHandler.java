package org.nortis.infrastructure.mvc;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * エラーを処理してエラーレスポンスを返すクラスです
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
	 * @param ex 例外
	 * @return エラーレスポンス
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DomainException.class)
	public ErrorResponse handleDomainException(DomainException ex) {
		return new ErrorResponse(LocalDateTime.now(), ex.resolveMessage(this.messageSource));
	}
	
	/**
	 * {@link UnexpectedException}を処理して、エラーレスポンスを返却します
	 * @param ex 例外
	 * @return エラーレスポンス
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UnexpectedException.class)
	public ErrorResponse handleUnexpectedException(UnexpectedException ex) {
		log.error("unexpected Error", ex);
		return new ErrorResponse(LocalDateTime.now(), ex.resolveMesage(messageSource));
	}
	
}
