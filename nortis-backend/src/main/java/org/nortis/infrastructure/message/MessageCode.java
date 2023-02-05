package org.nortis.infrastructure.message;

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
}
