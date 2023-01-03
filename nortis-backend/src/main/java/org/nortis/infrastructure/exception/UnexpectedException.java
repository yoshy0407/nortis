package org.nortis.infrastructure.exception;

import java.util.Arrays;
import lombok.Getter;
import org.nortis.infrastructure.MessageSourceAccessor;
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
	private final Object[] args;
	
	/**
	 * 例外を構築します
	 * @param messageId メッセージID
	 * @param args メッセージの引数
	 */
	public UnexpectedException(String messageId, Object... args) {
		this(messageId, null, args);
	}

	/**
	 * 例外を構築します
	 * @param messageId メッセージID
	 * @param cause ラップする例外
	 * @param args メッセージ引数
	 */
	public UnexpectedException(String messageId, Throwable cause, Object... args) {
		super(MessageSourceAccessor.getMessageSource()
				.getMessage(messageId, args, LocaleContextHolder.getLocale()), 
				cause);
		this.messageId = messageId;
		this.args = Arrays.copyOf(args, args.length);
	}

	/**
	 * 引数を返します
	 * @return 引数
	 */
	public Object[] getArgs() {
		return this.args;
	}

}
