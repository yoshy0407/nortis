package org.nortis.infrastructure.validation;

import java.util.regex.Pattern;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCode;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.util.StringUtils;

/**
 * 値の検証のユーティリティクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public final class Validations {

	/**
	 * コンストラクター
	 */
	private Validations() {
		throw new IllegalStateException("インスタンス化できません");
	}
	
	/**
	 * テキストが空文字でないことを確認します
	 * @param text テキスト
	 * @param valueName 値の名前
	 * @throws DomainException 空文字の場合
	 */
	public static void hasText(String text, String valueName) throws DomainException {
		if (!StringUtils.hasText(text)) {
			throw new DomainException(MessageCodes.nortis00001(valueName));
		}
	}
	
	/**
	 * テキストが指定文字以内であることを確認します
	 * @param text テキスト
	 * @param length 文字数
	 * @param valueName 値の名前
	 * @throws DomainException 文字数以上の場合
	 */
	public static void maxTextLength(String text, int length, String valueName) throws DomainException {
		if (text.length() > length) {
			throw new DomainException(MessageCodes.nortis00002(valueName, length));
		}
	}
	
	/**
	 * Nullでないことを確認します
	 * @param obj 値
	 * @param valueName 値の名前
	 * @throws DomainException nullの場合
	 */
	public static void notNull(Object obj, String valueName) throws DomainException {
		if (obj == null) {
			throw new DomainException(MessageCodes.nortis00001(valueName));			
		}
	}
	
	/**
	 * 正規表現によるチェックを実施します
	 * @param text テキスト
	 * @param regex 正規表現
	 * @param messageCode {@link MessageCode}
	 * @throws DomainException 正規表現に一致しない場合
	 */
	public static void regex(String text, String regex, MessageCode messageCode) throws DomainException {
		if (!Pattern.matches(regex, text)) {
			throw new DomainException(messageCode);
		}
	}
	
	/**
	 * 文字列が含まれているかチェックします
	 * @param text テキスト
	 * @param containText チェックする文字列
	 * @param messageCode {@link MessageCode}
	 * @throws DomainException 文字列が含まれない場合
	 */
	public static void contains(String text, String containText, MessageCode messageCode) throws DomainException {
		if (!text.contains(containText)) {
			throw new DomainException(messageCode);			
		}
	}
	
}
