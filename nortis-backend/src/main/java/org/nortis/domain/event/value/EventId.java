package org.nortis.domain.event.value;

import java.util.UUID;

import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "create")
public class EventId {

	private final String value;
	
	private EventId(String eventId) {
		Validations.hasText(eventId, "イベントID");
		this.value = eventId;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public static EventId create(String eventId) {
		return new EventId(eventId);
	}
	
	public static EventId createNew() {
		return new EventId(UUID.randomUUID().toString());
	}
	
}
