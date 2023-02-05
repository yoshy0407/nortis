package org.nortis.infrastructure.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class ValidationsTest {

	@Autowired
	MessageSource messageSource;
	
	@Test
	void testHasText() {
		assertDoesNotThrow(() -> {
			Validations.hasText("text", "値");
		});
	}

	@Test
	void testHasTextError() {
		assertThrows(DomainException.class, () -> {
			Validations.hasText("", "値");			
		}, "値が設定されていません");
	}

	@Test
	void testMaxTextLength() {
		assertDoesNotThrow(() -> {
			Validations.maxTextLength("text", 10, "値");
		});
	}

	@Test
	void testMaxTextLengthEqual() {
		assertDoesNotThrow(() -> {
			Validations.maxTextLength("text", 4, "値");
		});
	}

	@Test
	void testMaxTextLengthError() {
		assertThrows(DomainException.class, () -> {
			Validations.maxTextLength("1234567", 6, "値");	
		}, "値は6文字以下である必要があります");
	}
	
	@Test
	void testNotNull() {
		assertDoesNotThrow(() -> {
			Validations.notNull(new Object(), "値");
		});
	}

	@Test
	void testNotNullError() {
		assertThrows(DomainException.class, () -> {
			Validations.notNull(null, "値");
		}, "値が設定されていません");
	}
	
	@Test
	void testRegex() {
		assertDoesNotThrow(() -> {
			Validations.regex("123-0033", "^[0-9]{3}-[0-9]{4}$", MessageCodes.nortis10001());
		});
	}

	@Test
	void testRegexError() {
		assertThrows(DomainException.class, () -> {
			Validations.regex("1123-0033", "^[0-9]{3}-[0-9]{4}$", MessageCodes.nortis10001());
		});
	}
	
	@Test
	void testContains() {
		assertDoesNotThrow(() -> {
			Validations.contains("1234567", "1234", MessageCodes.nortis10001());
		});
	}

	@Test
	void testContainsError() {
		assertThrows(DomainException.class, () -> {
			Validations.contains("1234567", "QWE", MessageCodes.nortis10001());
		});
	}

}
