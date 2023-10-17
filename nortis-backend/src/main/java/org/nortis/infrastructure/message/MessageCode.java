package org.nortis.infrastructure.message;

import java.util.Locale;
import org.springframework.context.MessageSource;

/**
 * メッセージコードをあらすインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface MessageCode {

	/**
	 * メッセージコードを返却します
	 * @return メッセージコード
	 */
	String getCode();
	
	/**
	 * メッセージの引数を解決します
	 * @return メッセージの引数
	 */
	Object[] getArgs();
	
	/**
	 * デフォルトのメッセージを返却します
	 * @return デフォルトのメッセージ
	 */
	String getDefaultMessage();
	
	/**
	 * メッセージを解決して返却します
	 * @param messageSource {@link MessageSource}
	 * @param locale ロケール
	 * @return 解決したメッセージ
	 */
	String resolveMessage(MessageSource messageSource, Locale locale);
}
