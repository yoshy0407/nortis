package org.nortis.domain.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.exception.DomainException;

class ReceiveEventCheckDomainServiceTest {
	
	ReceiveEventCheckDomainService domainService;
	
	TenantRepository tenantRepository;
	
	EndpointRepository endpointRepository;
	
	@BeforeEach
	void setup() {
		this.tenantRepository = Mockito.mock(TenantRepository.class);
		this.endpointRepository = Mockito.mock(EndpointRepository.class);
		this.domainService = new ReceiveEventCheckDomainService(this.tenantRepository, this.endpointRepository);
	}
	
	@Test
	void testCheckBeforeRegisterSuccess() throws DomainException {
		TenantId tenantId = TenantId.create("TENANT1");
		EndpointId endpointId = EndpointId.create("TEST1");
		
		Mockito.when(this.tenantRepository.get(eq(tenantId)))
			.thenReturn(Optional.of(new Tenant()));

		Mockito.when(this.endpointRepository.get(eq(tenantId), eq(endpointId)))
			.thenReturn(Optional.of(new Endpoint()));

		assertDoesNotThrow(() -> {
			this.domainService.checkBeforeRegister(tenantId, endpointId);			
		});
	}

	@Test
	void testCheckBeforeRegisterTenantNotFound() throws DomainException {
		TenantId tenantId = TenantId.create("TENANT1");
		EndpointId endpointId = EndpointId.create("TEST1");
		
		Mockito.when(this.tenantRepository.get(eq(tenantId)))
			.thenReturn(Optional.empty());

		Mockito.when(this.endpointRepository.get(eq(tenantId), eq(endpointId)))
			.thenReturn(Optional.of(new Endpoint()));

		assertThrows(DomainException.class, () -> {
			this.domainService.checkBeforeRegister(tenantId, endpointId);			
		}, "指定されたIDのテナントは存在しません");
	}

	@Test
	void testCheckBeforeRegisterEndpointNotFound() throws DomainException {
		TenantId tenantId = TenantId.create("TENANT1");
		EndpointId endpointId = EndpointId.create("TEST1");
		
		Mockito.when(this.tenantRepository.get(eq(tenantId)))
			.thenReturn(Optional.of(new Tenant()));

		Mockito.when(this.endpointRepository.get(eq(tenantId), eq(endpointId)))
			.thenReturn(Optional.empty());

		assertThrows(DomainException.class, () -> {
			this.domainService.checkBeforeRegister(tenantId, endpointId);			
		}, "指定されたエンドポイントは存在しません。");
	}

}
