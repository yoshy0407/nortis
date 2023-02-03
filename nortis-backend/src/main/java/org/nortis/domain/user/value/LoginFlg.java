package org.nortis.domain.user.value;

import org.seasar.doma.Domain;

/**
 * ログインフラグ
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, accessorMethod = "getValue", factoryMethod = "resolve")
public enum LoginFlg {
	/**
	 * ログイン済み
	 */
	LOGIN("1"),
	/**
	 * 未ログイン
	 */
	NOT_LOGIN("0");
	
	private final String value;
	
	private LoginFlg(String value) {
		this.value = value;
	}
	
	/**
	 * 値を返却します
	 * @return 値
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * 値に対応するログインフラグを取得します
	 * @param value 値
	 * @return ログインフラグ
	 */
	public static LoginFlg resolve(String value) {
		for (LoginFlg loginFlg : LoginFlg.values()) {
			if (loginFlg.getValue().equals(value)) {
				return loginFlg;
			}
		}
		throw new IllegalStateException("指定された値が存在しません。　値：" + value);
	}
	
}
