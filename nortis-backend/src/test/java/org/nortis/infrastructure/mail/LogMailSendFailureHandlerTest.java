package org.nortis.infrastructure.mail;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.spi.LoggingEvent;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailSendException;
import test.LogMockit;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class LogMailSendFailureHandlerTest {

	@Autowired
	MessageSource messageSource;
	
	LogMailSendFailureHandler handler;
	
	LogMockit logMockit;
	
	@BeforeEach
	void setup() {
		LocaleContextHolder.setLocale(Locale.JAPAN);
		this.handler = new LogMailSendFailureHandler(messageSource);
		this.logMockit = new LogMockit(LogMailSendFailureHandler.class);
	}
	
	@Test
	void testHandleMessageError() {
		this.handler.handleMessageError(new MessagingException("test message"));
		
		List<LoggingEvent> events = this.logMockit.verify();
		
		assertThat(events).hasSize(1);
		assertThat(events.get(0).getMessage()).isEqualTo("メールのメッセージの作成に失敗しました");
	}

	@Test
	void testHandleSendError() {
		this.handler.handleSendError(new MailSendException("test message"));
		
		List<LoggingEvent> events = this.logMockit.verify();
		
		assertThat(events).hasSize(1);
		assertThat(events.get(0).getMessage()).isEqualTo("メールの送信に失敗しました");
	}

}
