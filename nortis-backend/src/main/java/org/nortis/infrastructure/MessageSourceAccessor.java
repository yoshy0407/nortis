package org.nortis.infrastructure;

import org.springframework.context.MessageSource;

/**
 * {@link MessageSource}へのアクセッサクラスです
 * @author yoshiokahiroshi
 * @since 1.0.0
 */
public final class MessageSourceAccessor {

	/**
	 * {@link MessageSource}
	 */
	private static MessageSource messageSource;
	
	/**
	 * コンストラクター
	 */
	private MessageSourceAccessor() {
		throw new IllegalStateException("インスタンス化できません");
	}
	
	/**
	 * {@link MessageSource}を設定します
	 * @param messageSource {@link MessageSource}
	 */
	public static void set(MessageSource messageSource) {
		MessageSourceAccessor.messageSource = messageSource;
	}
	
	/**
	 * {@link MessageSource}を返します
	 * @return {@link MessageSource}
	 */
	public static MessageSource getMessageSource() {
		return messageSource;
	}
	
}
