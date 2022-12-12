package org.nortis.infrastructure.exception;

import org.nortis.infrastructure.MessageSourceAccessor;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.Getter;

/**
 * ドメインに関する例外クラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DomainException extends RuntimeException {

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
	public DomainException(String messageId, Object...args) {
		this(messageId, null, args);
	}

	/**
	 * 例外を構築します
	 * @param messageId メッセージID
	 * @param cause ラップする例外
	 * @param args メッセージ引数
	 */
	public DomainException(String messageId, Throwable cause, Object...args) {
		super(MessageSourceAccessor.getMessageSource().getMessage(messageId, args, LocaleContextHolder.getLocale()), cause);
		this.messageId = messageId;
		this.args = args;
	}

	/**
	 * 引数を返します
	 * @return 引数
	 */
	public Object[] getArgs() {
		return new Object[]{this.args};
	}
	
}
