package org.nortis.infrastructure.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

class DomainExceptionTest {

	StaticMessageSource messageSource;
	
	@BeforeEach
	void setup() {
		LocaleContextHolder.setDefaultLocale(Locale.JAPAN);
		messageSource = new StaticMessageSource();
		messageSource.addMessage("messageId", LocaleContextHolder.getLocale(), "test message");
	}
	
	@Test
	void test() {
		DomainException ex = new DomainException("message", "messageId", 1, 2, 3);
		
		assertThat(ex.getMessageId()).isEqualTo("messageId");
		
		assertThat(ex.getArgs()).hasSize(3);
		assertThat(ex.getArgs()).contains(1, 2, 3);

		assertThat(ex.getMessage()).isEqualTo("message");
		assertThat(ex.resolveMessage(this.messageSource)).isEqualTo("test message");
	}

}
