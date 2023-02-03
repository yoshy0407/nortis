package org.nortis.infrastructure.utils;

import java.util.Random;

/**
 * ランダム文字列を作成するクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class RandomString {

	private static final String WORD_PATTERN = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	private final int length;
	
	private RandomString(int length) {
		this.length = length;
	}
	
	/**
	 * 構築します
	 * @return 構築した文字列
	 */
	public String build() {
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			builder.append(WORD_PATTERN.charAt(random.nextInt(WORD_PATTERN.length())));
		}
		return builder.toString();
	}
	
	/**
	 * デフォルト文字桁数で作成します
	 * @return 作成したインスタンス
	 */
	public static RandomString defaultLength() {
		return new RandomString(10);
	}
	
	/**
	 * 桁数を指定してインスタンスを作成します
	 * @param length 桁数
	 * @return 作成したインスタンス
	 */
	public static RandomString of(int length) {
		return new RandomString(length);
	}
	
}
