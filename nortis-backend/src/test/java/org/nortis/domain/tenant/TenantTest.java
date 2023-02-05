package org.nortis.domain.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

class TenantTest {

	@Autowired
	MessageSource messageSource;

	@Test
	void testChangeTenantName() throws DomainException {
		Tenant tenant = Tenant.create(TenantId.create("TEST1"), "TEST1 Tenant", "TEST_ID");

		tenant.changeTenantName("TEST11 Tenant", "TEST_ID2");

		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TEST1"));
		assertThat(tenant.getTenantName()).isEqualTo("TEST11 Tenant");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNotNull();
		assertThat(tenant.getUpdateId()).isEqualTo("TEST_ID2");
		//Domaによって更新されるため、同じになる
		assertThat(tenant.getVersion()).isEqualTo(1L);
	}

	@Test
	void testCreateEndpoint() throws DomainException {
		Tenant tenant = Tenant.create(TenantId.create("TEST1"), "TEST1 Tenant", "TEST_ID");

		Endpoint endpoint = tenant.createEndpoint(
				EndpointId.create("ENDPOINT"), 
				"TEST_ENDPOINT", 
				"subject", 
				"body", 
				"USER_ID");
		
		assertThat(endpoint.getEndpointId()).isEqualTo(EndpointId.create("ENDPOINT"));
		assertThat(endpoint.getTenantId()).isEqualTo(TenantId.create("TEST1"));
		assertThat(endpoint.getEndpointName()).isEqualTo("TEST_ENDPOINT");
		assertThat(endpoint.getSubjectTemplate()).isEqualTo("subject");
		assertThat(endpoint.getMessageTemplate()).isEqualTo("body");
		assertThat(endpoint.getCreateId()).isEqualTo("USER_ID");
		assertThat(endpoint.getCreateDt()).isNotNull();
		assertThat(endpoint.getUpdateId()).isNull();
		assertThat(endpoint.getUpdateDt()).isNull();
		assertThat(endpoint.getVersion()).isEqualTo(1L);
	}

	@Test
	void testDeleted() throws DomainException {
		Tenant tenant = Tenant.create(TenantId.create("TEST1"), "TEST1 Tenant", "TEST_ID");

		tenant.deleted("USER_ID");

		assertThat(tenant.getUpdateId()).isEqualTo("USER_ID");
		assertThat(tenant.getUpdateDt()).isBefore(LocalDateTime.now());
	}

	@Test
	void testCreate() throws DomainException {
		Tenant tenant = Tenant.create(TenantId.create("TEST"), "TEST TENANT", "TEST_ID");

		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(tenant.getTenantName()).isEqualTo("TEST TENANT");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNull();
		assertThat(tenant.getUpdateId()).isNull();
		assertThat(tenant.getVersion()).isEqualTo(1L);
	}
	
	@Test
	void testCreateApiKey() throws DomainException {
		Tenant tenant = Tenant.create(TenantId.create("TEST"), "TEST TENANT", "TEST_ID");
		Authentication auth = tenant.createApiKey();
		
		assertThat(auth.getApiKey()).isNotNull();
		assertThat(auth.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(auth.getUserId()).isNull();
	}

	@Test
	void testCreateTenantIdNull() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(null, "", "TEST_ID");

		}, "テナントIDが未設定です");
	}

	@Test
	void testCreateTenantNameEmpty() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.create("TEST"), "", "TEST_ID");
		}, "テナント名が未設定です");
	}

	@Test
	void testCreateTenantNameNull() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.create("TEST"), null, "TEST_ID");
		}, "テナント名が未設定です");
	}

	@Test
	void testCreateTenantNameLength() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.create("TEST"), "123456789012345678901234567890123456789012345678901", "TEST_ID");

		}, "テナント名は50文字以内である必要があります");
	}

	@Test
	void testCreateCreateIdNull() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.create("TEST"), "123456789012345678901234567890123456789012345678901", "");

		}, "作成者IDが未設定です");
	}

}
