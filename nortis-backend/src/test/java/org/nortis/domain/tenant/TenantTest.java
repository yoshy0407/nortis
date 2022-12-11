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
		Tenant tenant = Tenant.create(TenantId.of("TEST1"), "TEST1 Tenant", "TEST_ID");

		tenant.changeTenantName("TEST11 Tenant", "TEST_ID2");

		assertThat(tenant.getUuid()).isNotNull();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.of("TEST1"));
		assertThat(tenant.getTenantName()).isEqualTo("TEST11 Tenant");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNotNull();
		assertThat(tenant.getUpdateId()).isEqualTo("TEST_ID2");
		assertThat(tenant.getVersion()).isEqualTo(2L);
	}

	@Test
	void testChangeTenantShortName() {
		Tenant tenant = Tenant.create(TenantId.of("TEST2"), "TEST3 Tenant", "TEST_ID");

		tenant.changeTenantId(TenantId.of("TEST_ID"), "TEST_ID2");

		assertThat(tenant.getUuid()).isNotNull();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.of("TEST_ID"));
		assertThat(tenant.getTenantName()).isEqualTo("TEST3 Tenant");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNotNull();
		assertThat(tenant.getUpdateId()).isEqualTo("TEST_ID2");
		assertThat(tenant.getVersion()).isEqualTo(2L);
	}

	@Test
	void testCreate() {
		Tenant tenant = Tenant.create(TenantId.of("TEST"), "TEST TENANT", "TEST_ID");

		assertThat(tenant.getUuid()).isNotNull();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.of("TEST"));
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
			Tenant.create(TenantId.of("TEST"), "", "TEST_ID");
		}, "テナント名が未設定です");
	}

	@Test
	void testCreateTenantNameNull() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.of("TEST"), null, "TEST_ID");
		}, "テナント名が未設定です");
	}

	@Test
	void testCreateTenantNameLength() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.of("TEST"), "123456789012345678901234567890123456789012345678901", "TEST_ID");
			
		}, "テナント名は50文字以内である必要があります");
	}

	@Test
	void testCreateCreateIdNull() {
		assertThrows(DomainException.class, () -> {
			Tenant.create(TenantId.of("TEST"), "123456789012345678901234567890123456789012345678901", "");
			
		}, "作成者IDが未設定です");
	}

}
