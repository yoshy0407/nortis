package org.nortis.domain.event.value;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * イベントID
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public final class EventId {

	/** イベントIDの値 */
	private final String value;
	
	/**
	 * コンストラクター
	 * @param eventId イベントID
	 */
	private EventId(String eventId) {
		Validations.hasText(eventId, "イベントID");
		this.value = eventId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
	/**
	 * 値を元にイベントIDオブジェクトを生成します
	 * @param eventId イベントID
	 * @return イベントIDオブジェクト
	 */
	public static EventId create(String eventId) {
		return new EventId(eventId);
	}
	
	/**
	 * 新しい値を採番します
	 * @return イベントID
	 */
	public static EventId createNew() {
		return new EventId(UUID.randomUUID().toString());
	}
	
}
