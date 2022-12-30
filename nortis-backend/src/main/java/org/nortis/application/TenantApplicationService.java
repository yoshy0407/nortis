package org.nortis.application;

import java.util.Optional;

import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ApplicationService
public class TenantApplicationService {

	private final TenantRepository tenantRepository;
	
	private final TenantDomainService tenantDomainService;
	
	public <R> R register(
			String inTenantId,
			String name,
			String userId,
			ApplicationTranslator<Tenant, R> translator) {
		
		TenantId tenantId = TenantId.create(inTenantId);
		Tenant tenant = this.tenantDomainService.createTenant(tenantId, name, userId);
		
		this.tenantRepository.save(tenant);
		
		return translator.translate(tenant);
	}
	
	public <R> R changeName(
			String inTenantId, 
			String name, 
			String userId, 
			ApplicationTranslator<Tenant, R> translator) {
		
		TenantId tenantId = TenantId.create(inTenantId);

		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		
		Tenant tenant = optTenant.get();
		
		tenant.changeTenantName(name, userId);
		
		this.tenantRepository.update(tenant);
		
		return translator.translate(tenant);
	}
	
	public void delete(String inTenantId) {
		TenantId tenantId = TenantId.create(inTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		this.tenantRepository.remove(optTenant.get());
	}
}
