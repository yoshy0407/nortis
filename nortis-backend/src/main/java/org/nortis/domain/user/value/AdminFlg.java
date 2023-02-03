package org.nortis.domain.user.value;

import org.seasar.doma.Domain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 管理者フラグ
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, accessorMethod = "getValue", factoryMethod = "resolve")
public enum AdminFlg {
	/**
	 * 管理者ユーザ
	 */
	ADMIN("1"),
	/**
	 * 通常ユーザ
	 */
	MEMBER("0");
	
	private final String value;
	
	private AdminFlg(String value) {
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
	 * 権限を返却します
	 * @return 権限
	 */
	public GrantedAuthority getAuthority() {
		return new SimpleGrantedAuthority(this.name());
	}
	
	/**
	 * 値に対応するログインフラグを取得します
	 * @param value 値
	 * @return ログインフラグ
	 */
	public static AdminFlg resolve(String value) {
		for (AdminFlg loginFlg : AdminFlg.values()) {
			if (loginFlg.getValue().equals(value)) {
				return loginFlg;
			}
		}
		throw new IllegalStateException("指定された値が存在しません。　値：" + value);
	}
	
}
