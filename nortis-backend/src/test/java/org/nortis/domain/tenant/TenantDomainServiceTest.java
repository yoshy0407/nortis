package org.nortis.domain.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;

@SpringBootTest(classes = MessageSourceAutoConfiguration.class)
class TenantDomainServiceTest {

	@Autowired
	MessageSource messageSource;
	
	TenantRepository repository;
	
	TenantDomainService domainService;
	
	@BeforeEach
	void setup() {
		MessageSourceAccessor.set(messageSource);
		this.repository = Mockito.mock(TenantRepository.class);
		this.domainService = new TenantDomainService(repository);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testCreateTenant() {
		when(this.repository.findAll(any(Example.class))).thenReturn(new ArrayList<>());
		
		Tenant tenant = this.domainService.createTenant(TenantId.of("TEST"), "テナント", "TEST_ID");
	
		assertThat(tenant.getUuid()).isNotNull();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.of("TEST"));
		assertThat(tenant.getTenantName()).isEqualTo("テナント");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNull();
		assertThat(tenant.getUpdateId()).isNull();
		assertThat(tenant.getVersion()).isEqualTo(1L);

	}

	@SuppressWarnings("unchecked")
	@Test
	void testCreateTenantError() {
		List<Tenant> tenantList = new ArrayList<>();
		tenantList.add(Tenant.create(TenantId.of("TEST"), "テナント", "TEST_ID"));
		
		when(this.repository.findAll(any(Example.class))).thenReturn(tenantList);
		
		assertThrows(DomainException.class, () -> {
			this.domainService.createTenant(TenantId.of("TEST"), "テナント", "TEST_ID");
		}, "指定されたテナントIDはすでに使われています");
	}
	
	@Test
	void testChangeTenantId() {

		when(this.repository.findByTenantId(eq(TenantId.of("TEST_ID")))).thenReturn(new ArrayList<>());

		List<Tenant> tenantList = new ArrayList<>();
		tenantList.add(Tenant.create(TenantId.of("TEST"), "テナント", "TEST_ID"));

		when(this.repository.findByTenantId(eq(TenantId.of("TEST")))).thenReturn(tenantList);

		Tenant tenant = this.domainService.changeTenantId(TenantId.of("TEST"), TenantId.of("TESTID"), "TEST_ID");
	
		assertThat(tenant.getUuid()).isNotNull();
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.of("TESTID"));
		assertThat(tenant.getTenantName()).isEqualTo("テナント");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNotNull();
		assertThat(tenant.getUpdateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getVersion()).isEqualTo(2L);
	}
	
	@Test
	void testChangeTenantIdCheckError() {
		List<Tenant> tenantList = new ArrayList<>();
		tenantList.add(Tenant.create(TenantId.of("TESTID"), "テナント", "TEST_ID"));

		when(this.repository.findByTenantId(eq(TenantId.of("TESTID")))).thenReturn(tenantList);

		assertThrows(DomainException.class, () -> {
			this.domainService.changeTenantId(TenantId.of("TEST"), TenantId.of("TESTID"), "TEST_ID");
		}, "指定されたテナントIDはすでに使われています");
	}

	@Test
	void testChangeTenantIdCurrentError() {

		when(this.repository.findByTenantId(eq(TenantId.of("TEST_ID")))).thenReturn(new ArrayList<>());

		List<Tenant> tenantList = new ArrayList<>();
		tenantList.add(Tenant.create(TenantId.of("TEST"), "テナント", "TEST_ID"));
		tenantList.add(Tenant.create(TenantId.of("TEST"), "テナント", "TEST_ID"));

		when(this.repository.findByTenantId(eq(TenantId.of("TEST")))).thenReturn(tenantList);

		assertThrows(UnexpectedException.class, () -> {
			this.domainService.changeTenantId(TenantId.of("TEST"), TenantId.of("TESTID"), "TEST_ID");
		});
	}

}
