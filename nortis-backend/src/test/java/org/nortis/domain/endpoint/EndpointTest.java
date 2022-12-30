package org.nortis.domain.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class EndpointTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}

	@Test
	void testChangeEndpointName() {
		Endpoint endpoint = Endpoint.create(EndpointId.create("ENDPOINT"), TenantId.create("TEST"), "Test Endpoint", "TEST_ID");
		endpoint.changeEndpointName("Endpoint", "TEST_ID");
		
		assertThat(endpoint.getEndpointName()).isEqualTo("Endpoint");
	}

	@Test
	void testCreate() {
		Endpoint endpoint = Endpoint.create(EndpointId.create("ENDPOINT"), TenantId.create("TEST"), "Test Endpoint", "TEST_ID");
		
		assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("ENDPOINT"));
		assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(endpoint.getEndpointName()).isEqualTo("Test Endpoint");
		assertThat(endpoint.getCreateId()).isEqualTo("TEST_ID");
		assertThat(endpoint.getCreateDt()).isNotNull();
		assertThat(endpoint.getUpdateId()).isNull();
		assertThat(endpoint.getUpdateDt()).isNull();
		assertThat(endpoint.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateEndpointIdNull() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(null, TenantId.create("TEST"), "Test Endpoint", "TEST_ID");			
		}, "エンドポイントIDが未設定です");		
	}

	@Test
	void testCreateTenantIdNull() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), null, "Test Endpoint", "TEST_ID");			
		}, "テナントIDが未設定です");		
	}

	@Test
	void testCreateEndpointNameNull() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), TenantId.create("TEST"), null, "TEST_ID");			
		}, "エンドポイント名が未設定です");		
	}

	@Test
	void testCreateEndpointNameEmpty() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), TenantId.create("TEST"), "", "TEST_ID");			
		}, "エンドポイント名が未設定です");		
	}

	@Test
	void testCreateEndpointNameMaxLength() {
		assertThrows(DomainException.class, () -> {
			Endpoint.create(EndpointId.create("ENDPOINT"), TenantId.create("TEST"), "123456789012345678901234567890123456789012345678901", "TEST_ID");			
		}, "エンドポイント名は50文字以内である必要があります");
	}

	
}
