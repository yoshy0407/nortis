package org.nortis.domain.event.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EventIdTest {

	@Test
	void testHashCode() {
		assertThat(EventId.create("test-ID").hashCode()).isEqualTo(EventId.create("test-ID").hashCode());
		assertThat(EventId.create("test-ID").hashCode()).isNotEqualTo(EventId.create("test123-ID").hashCode());
	}

	@Test
	void testToString() {
		assertThat(EventId.create("test-ID").toString()).isEqualTo("test-ID");
	}

	@Test
	void testCreateNew() {
		assertThat(EventId.createNew().toString().length()).isEqualTo(36);
	}

	@Test
	void testEqualsObject() {
		assertThat(EventId.create("test-ID").equals(EventId.create("test-ID"))).isTrue();
		assertThat(EventId.create("test-ID").equals(EventId.create("test123-ID"))).isFalse();
	}

}
