package org.nortis.infrastructure.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

class UnexpectedExceptionTest {

	StaticMessageSource messageSource;
	
	@BeforeEach
	void setup() {
		LocaleContextHolder.setLocale(Locale.JAPAN);

		this.messageSource = new StaticMessageSource();
		this.messageSource.addMessage("messageId", LocaleContextHolder.getLocale(), "test message");
	}
	
	@Test
	void test() {
		UnexpectedException ex = new UnexpectedException("message", "messageId", 1, 2, 3);
		
		assertThat(ex.getMessageId()).isEqualTo("messageId");
		
		assertThat(ex.getArgs()).hasSize(3);
		assertThat(ex.getArgs()).contains(1, 2, 3);

		assertThat(ex.getMessage()).isEqualTo("message");
		
		assertThat(ex.resolveMessage(messageSource)).isEqualTo("test message");
	}

}
