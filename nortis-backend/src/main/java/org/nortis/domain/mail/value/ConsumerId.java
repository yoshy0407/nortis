package org.nortis.domain.mail.value;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.seasar.doma.Domain;

/**
 * コンシューマID
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public final class ConsumerId {

	/** コンシューマIDの値 */
	private final String value;
	
	/**
	 * コンストラクター
	 * @param consumerId コンシューマID
	 */
	private ConsumerId(String consumerId) {
		this.value = consumerId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
	/**
	 * 値からコンシューマIDを作成します
	 * @param consumerId コンシューマID
	 * @return コンシューマID
	 */
	public static ConsumerId create(String consumerId) {
		return new ConsumerId(consumerId);
	}
	
	/**
	 * 新しいコンシューマIDを採番します
	 * @return コンシューマID
	 */
	public static ConsumerId createNew() {
		return new ConsumerId(UUID.randomUUID().toString());
	}
}
