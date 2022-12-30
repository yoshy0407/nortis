package org.nortis.domain.mail.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MailTypeTest {

	@Test
	void testToString() {
		assertThat(MailType.HTML.toString()).isEqualTo("HTML");
		assertThat(MailType.TEXT.toString()).isEqualTo("TEXT");
	}

	@Test
	void testCreate() {
		assertThat(MailType.create("HTML")).isEqualTo(MailType.HTML);
		assertThat(MailType.create("TEXT")).isEqualTo(MailType.TEXT);
		assertThrows(IllegalArgumentException.class, () -> {
			MailType.create("hogehoge");
		});
	}

}
