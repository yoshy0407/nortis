package org.nortis.infrastructure;

import org.springframework.context.MessageSource;

/**
 * {@link MessageSource}へのアクセッサクラスです
 * @author yoshiokahiroshi
 * @since 1.0.0
 */
public class MessageSourceAccessor {

	private static MessageSource messageSource;
	
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
	public static MessageSource messageSource() {
		return messageSource;
	}
	
}
