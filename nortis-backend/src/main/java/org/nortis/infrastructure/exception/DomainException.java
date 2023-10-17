package org.nortis.infrastructure.exception;

import java.util.Arrays;
import lombok.Getter;
import org.nortis.infrastructure.message.MessageCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * ドメインに関する例外クラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DomainException extends Exception {

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
	 * @param messageCode {@link MessageCode}
	 */
	public DomainException(MessageCode messageCode) {
		this(messageCode, null);
	}
	
	/**
	 * 例外を構築します
	 * @param messageCode {@link MessageCode}
	 * @param cause 例外クラス
	 */
	public DomainException(MessageCode messageCode, Throwable cause) {
		this(messageCode.getDefaultMessage(),
				messageCode.getCode(),
				cause,
				messageCode.getArgs());
	}
	
	/**
	 * 例外を構築します
	 * @param defaultMessage デフォルトメッセージ
	 * @param messageId メッセージID
	 * @param args メッセージの引数
	 */
	public DomainException(String defaultMessage, String messageId, Object... args) {
		this(defaultMessage, messageId, null, args);
	}

	/**
	 * 例外を構築します
	 * @param defaultMessage デフォルトメッセージ
	 * @param messageId メッセージID
	 * @param cause ラップする例外
	 * @param args メッセージ引数
	 */
	public DomainException(String defaultMessage, String messageId, Throwable cause, Object... args) {
		super(defaultMessage, cause);
		this.messageId = messageId;
		this.args = Arrays.copyOf(args, args.length);
	}

	/**
	 * {@link MessageSource}を使用してメッセージを解決します
	 * @param messageSource {@link MessageSource}
	 * @return 解決したメッセージ
	 */
	public String resolveMessage(MessageSource messageSource) {
		return messageSource.getMessage(messageId, args, getMessage(), LocaleContextHolder.getLocale());
	}
	
	/**
	 * ログ出力用にフォーマットしたメッセージを解決します
	 * @param messageSource {@link MessageSource}
	 * @return 解決したメッセージ
	 */
	public String resolveLogFormatMessage(MessageSource messageSource) {
		return new StringBuilder()
				.append("[%s]".formatted(this.messageId))
				.append(resolveMessage(messageSource))
				.toString();
	}
	
}
