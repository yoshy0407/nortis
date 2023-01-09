package org.nortis.domain.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDateTime;
import org.apache.velocity.app.VelocityEngine;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.SendMessage;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.template.TemplateRender;
import org.nortis.infrastructure.template.VelocityTemplateRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class EndpointTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
		
		ApplicationContext context = Mockito.mock(ApplicationContext.class);
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		Mockito.when(context.getBean(eq(TemplateRender.class))).thenReturn(new VelocityTemplateRender(ve));
		ApplicationContextAccessor.set(context);
	}

	@Test
	void testChangeEndpointName() {
		Endpoint endpoint = Endpoint.create(EndpointId.create("ENDPOINT"), 
				TenantId.create("TEST"), "Test Endpoint", "subject", "message", "TEST_ID");
		endpoint.changeEndpointName("Endpoint", "TEST_ID");
		
		assertThat(endpoint.getEndpointName()).isEqualTo("Endpoint");
		assertThat(endpoint.getUpdateId()).isEqualTo("TEST_ID");
	}

	@Test
	void testChangeSubjectTemplate() {
		Endpoint endpoint = Endpoint.create(EndpointId.create("ENDPOINT"), 
				TenantId.create("TEST"), "Test Endpoint", "subject", "message", "TEST_ID");
		endpoint.changeSubjectTemplate("updated!", "TEST_ID");
		
		assertThat(endpoint.getSubjectTemplate()).isEqualTo("updated!");
		assertThat(endpoint.getUpdateId()).isEqualTo("TEST_ID");
	}

	@Test
	void testChangeMessageTemplate() {
		Endpoint endpoint = Endpoint.create(EndpointId.create("ENDPOINT"), 
				TenantId.create("TEST"), "Test Endpoint", "subject", "message", "TEST_ID");
		endpoint.changeMessageTemplate("Test Message", "TEST_ID");
		
		assertThat(endpoint.getMessageTemplate()).isEqualTo("Test Message");
		assertThat(endpoint.getUpdateId()).isEqualTo("TEST_ID");
	}

	@Test
	void testRenderMessage() {
		Endpoint endpoint = 
				Endpoint.create(EndpointId.create("ENDPOINT"), 
						TenantId.create("TEST"), "Test Endpoint", "Test ${name}", "Hello! ${name}", "TEST_ID");
		SendMessage message = endpoint.renderMessage(Maps.newHashMap("name", "Taro"));
		
		assertThat(message.getSubject()).isEqualTo("Test Taro");
		assertThat(message.getMessage()).isEqualTo("Hello! Taro");
	}

	@Test
	void testDeleted() {
		Endpoint endpoint = 
				Endpoint.create(EndpointId.create("ENDPOINT"), 
						TenantId.create("TEST"), "Test Endpoint", "Test ${name}", "Hello! ${name}", "TEST_ID");
		
		endpoint.deleted("USER_ID");
		
		assertThat(endpoint.getUpdateId()).isEqualTo("USER_ID");
		assertThat(endpoint.getUpdateDt()).isBefore(LocalDateTime.now());
	}
	
	@Test
	void testCreate() {
		Endpoint endpoint = Endpoint.create(EndpointId.create("ENDPOINT"), 
				TenantId.create("TEST"), "Test Endpoint", "subject", "message", "TEST_ID");
		
		assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("ENDPOINT"));
		assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(endpoint.getEndpointName()).isEqualTo("Test Endpoint");
		assertThat(endpoint.getSubjectTemplate()).isEqualTo("subject");
		assertThat(endpoint.getMessageTemplate()).isEqualTo("message");
		assertThat(endpoint.getCreateId()).isEqualTo("TEST_ID");
		assertThat(endpoint.getCreateDt()).isNotNull();
		assertThat(endpoint.getUpdateId()).isNull();
		assertThat(endpoint.getUpdateDt()).isNull();
		assertThat(endpoint.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateEndpointIdNull() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(null, TenantId.create("TEST"), "Test Endpoint", "subject", "message", "TEST_ID");			
		}, "エンドポイントIDが未設定です");		
	}

	@Test
	void testCreateTenantIdNull() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), null, "Test Endpoint", "subject", "message", "TEST_ID");			
		}, "テナントIDが未設定です");		
	}

	@Test
	void testCreateEndpointNameNull() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), 
					TenantId.create("TEST"), null, "subject", "message", "TEST_ID");			
		}, "エンドポイント名が未設定です");		
	}

	@Test
	void testCreateEndpointNameEmpty() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), 
					TenantId.create("TEST"), "", "subject", "message", "TEST_ID");			
		}, "エンドポイント名が未設定です");		
	}

	@Test
	void testCreateEndpointNameMaxLength() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), TenantId.create("TEST"), 
					"123456789012345678901234567890123456789012345678901", 
					"subject", "message", "TEST_ID");			
		}, "エンドポイント名は50文字以内である必要があります");
	}

	
}
