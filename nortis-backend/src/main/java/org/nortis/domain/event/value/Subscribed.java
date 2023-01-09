package org.nortis.domain.event.value;

import org.seasar.doma.Domain;

/**
 * 受信済みであることを表すオブジェクト
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(accessorMethod = "toString", factoryMethod = "resolve", valueType = String.class)
public enum Subscribed {
	/**
	 * 真
	 */
	TRUE("1"),
	/**
	 * 偽
	 */
	FALSE("0");
	
	private final String value;
	
	private Subscribed(String value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
	/**
	 * 列挙型を解決します
	 * @param value 値
	 * @return 列挙型
	 */
	public static Subscribed resolve(String value) {
		for (Subscribed subscribed : Subscribed.values()) {
			if (subscribed.toString().equals(value)) {
				return subscribed;
			}
		}
		throw new IllegalArgumentException("指定された項目は存在しません。 値: " + value);
	}
}
