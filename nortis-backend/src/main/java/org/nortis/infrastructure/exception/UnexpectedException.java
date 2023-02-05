package org.nortis.infrastructure.exception;

import java.util.Arrays;
import lombok.Getter;
import org.nortis.infrastructure.message.MessageCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * 想定外のエラーが発生した場合の例外です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class UnexpectedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * メッセージID
	 */
	@Getter
	private final String messageId;
	
	/**
	 * メッセージ引数
	 */
	@Getter
	private final Object[] args;
	
	/**
	 * 例外を構築します
	 * @param messsageCode {@link MessageCode}
	 * @param cause 発生した例外クラス
	 */
	public UnexpectedException(MessageCode messsageCode, Throwable cause) {
		this(messsageCode.getDefaultMessage(), messsageCode.getCode(), cause, messsageCode.getArgs());
	}

	/**
	 * 例外を構築します
	 * @param defaultMessage デフォルトメッセージ
	 * @param messageId メッセージID
	 * @param args メッセージの引数
	 */
	public UnexpectedException(String defaultMessage, String messageId, Object... args) {
		this(defaultMessage, messageId, null, args);
	}

	/**
	 * 例外を構築します
	 * @param defaultMessage デフォルトメッセージ
	 * @param messageId メッセージID
	 * @param cause ラップする例外
	 * @param args メッセージ引数
	 */
	public UnexpectedException(String defaultMessage, String messageId, Throwable cause, Object... args) {
		super(defaultMessage, cause);
		this.messageId = messageId;
		this.args = Arrays.copyOf(args, args.length);
	}
	
	/**
	 * メッセージを解決します
	 * @param messageSource {@link MessageSource}
	 * @return 解決したメッセージ
	 */
	public String resolveMesage(MessageSource messageSource) {
		return messageSource.getMessage(messageId, args, getMessage(), LocaleContextHolder.getLocale());
	}

	/**
	 * {@link DomainException}を{@link UnexpectedException}に変換します
	 * @param ex ドメインロジックエラー
	 * @return {@link UnexpectedException}
	 */
	public static UnexpectedException convertDomainException(DomainException ex) {
		return new UnexpectedException(ex.getMessage(), ex.getMessageId(), ex, ex.getArgs());
	}
}
