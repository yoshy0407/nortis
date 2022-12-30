package org.nortis.domain.mail.value;

import java.util.UUID;

import org.seasar.doma.Domain;

import lombok.EqualsAndHashCode;

/**
 * コンシューマID
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public class ConsumerId {

	private final String value;
	
	private ConsumerId(String consumerId) {
		this.value = consumerId;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public static ConsumerId create(String consumerId) {
		return new ConsumerId(consumerId);
	}
	
	public static ConsumerId createNew() {
		return new ConsumerId(UUID.randomUUID().toString());
	}
}
