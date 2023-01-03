package org.nortis.infrastructure.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

class UnexpectedExceptionTest {

	@BeforeEach
	void setup() {
		LocaleContextHolder.setLocale(Locale.JAPAN);

		StaticMessageSource ms = new StaticMessageSource();
		ms.addMessage("messageId", LocaleContextHolder.getLocale(), "test message");
		MessageSourceAccessor.set(ms);		
	}
	
	@Test
	void test() {
		UnexpectedException ex = new UnexpectedException("messageId", 1, 2, 3);
		
		assertThat(ex.getMessageId()).isEqualTo("messageId");
		
		assertThat(ex.getArgs()).hasSize(3);
		assertThat(ex.getArgs()).contains(1, 2, 3);

		assertThat(ex.getMessage()).isEqualTo("test message");
	}

}
