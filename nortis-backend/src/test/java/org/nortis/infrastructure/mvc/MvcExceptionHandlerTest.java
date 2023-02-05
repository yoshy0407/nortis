package org.nortis.infrastructure.mvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.BeanNameViewResolver;

class MvcExceptionHandlerTest {

	MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		LocaleContextHolder.setLocale(Locale.JAPAN);
		StaticMessageSource messageSource = new StaticMessageSource();
		messageSource.addMessage(TestController.DOMAIN_ID, Locale.JAPAN, "domainExceptionTest");
		messageSource.addMessage(TestController.UNEXPECTED_ID, Locale.JAPAN, "unexpectedExceptionTest");
		
		this.mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
				.setControllerAdvice(new MvcExceptionHandler(messageSource))
				.setViewResolvers(new BeanNameViewResolver())
				.build();
	}
	
	@Test
	void testDomainException() throws Exception {
		this.mockMvc.perform(get("/test1")
				.locale(Locale.JAPAN))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.message").value("domainExceptionTest"));
	}

	@Test
	void testUnexpectedException() throws Exception {
		this.mockMvc.perform(get("/test2")
				.locale(Locale.JAPAN))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.message").value("unexpectedExceptionTest"));
	}

}
