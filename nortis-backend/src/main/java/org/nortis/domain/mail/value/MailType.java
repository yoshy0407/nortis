package org.nortis.domain.mail.value;

import org.seasar.doma.Domain;

@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public enum MailType {
	/**
	 * HTML形式
	 */
	HTML("HTML"),
	/**
	 * テキスト形式
	 */
	TEXT("TEXT");
	
	private final String value;
	
	private MailType(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
	
	public static MailType create(String value) {
		for (MailType mailType : MailType.values()) {
			if (mailType.value.equals(value)) {
				return mailType;
			}
		}
		throw new IllegalArgumentException("想定外の値です。:" + value);
	}
}
