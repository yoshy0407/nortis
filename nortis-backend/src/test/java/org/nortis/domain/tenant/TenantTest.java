package org.nortis.domain.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class TenantTest {

	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
	}

	@Test
	void testChangeTenantName() {
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
	void testCreate() {
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
