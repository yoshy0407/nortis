package org.nortis.domain.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;

class TenantDomainServiceTest {
	
	TenantRepository repository;
	
	TenantDomainService domainService;
	
	@BeforeEach
	void setup() {
		this.repository = Mockito.mock(TenantRepository.class);
		this.domainService = new TenantDomainService(repository);
	}
	
	@Test
	void testCreateTenant() throws DomainException {
		when(this.repository.get(eq(TenantId.create("TEST")))).thenReturn(Optional.empty());
		
		Tenant tenant = this.domainService
				.createTenant(TenantId.create("TEST"), "テナント", "TEST_ID");
	
		assertThat(tenant.getTenantId()).isEqualTo(TenantId.create("TEST"));
		assertThat(tenant.getTenantName()).isEqualTo("テナント");
		assertThat(tenant.getCreateDt()).isNotNull();
		assertThat(tenant.getCreateId()).isEqualTo("TEST_ID");
		assertThat(tenant.getUpdateDt()).isNull();
		assertThat(tenant.getUpdateId()).isNull();
		assertThat(tenant.getVersion()).isEqualTo(1L);

	}

	@Test
	void testCreateTenantError() throws DomainException {	
		when(this.repository.get(eq(TenantId.create("TEST"))))
			.thenReturn(Optional.of(
					Tenant.create(TenantId.create("TEST"), "テナント", "TEST_ID")));
		
		assertThrows(DomainException.class, () -> {
			this.domainService.createTenant(TenantId.create("TEST"), "テナント", "TEST_ID");
		}, "指定されたテナントIDはすでに使われています");
	}
	
	@Test
	void testExistsTenantTrue() throws DomainException {
		when(this.repository.get(eq(TenantId.create("TEST"))))
			.thenReturn(Optional.of(
					Tenant.create(TenantId.create("TEST"), "テナント", "TEST_ID")));
		
		assertThat(this.domainService.existTenant(TenantId.create("TEST"))).isTrue();
	}
	
	@Test
	void testExistsTenantFalse() throws DomainException {
		when(this.repository.get(eq(TenantId.create("TEST"))))
			.thenReturn(Optional.empty());
		
		assertThat(this.domainService.existTenant(TenantId.create("TEST"))).isFalse();
	}
}
