package org.nortis.application;

import java.util.Optional;

import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

import lombok.AllArgsConstructor;

/**
 * エンドポイントのアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class EndpointApplicationService {

	private final TenantRepository tenantRepository;
	
	private final EndpointRepository endpointRepository;
	
	public <R> R registerEndpoint(
			String inTenantId, 
			String inEndpointId, 
			String endpointName, 
			String userId,
			ApplicationTranslator<Endpoint, R> translator) {
		
		TenantId tenantId = TenantId.create(inTenantId);
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		
		EndpointId endpointId = EndpointId.create(inEndpointId);
		
		Endpoint endpoint = tenant.get().createEndpoint(endpointId, endpointName, userId);
		
		this.endpointRepository.save(endpoint);
		
		return translator.translate(endpoint);
	}
	
	public <R> R changeName(
			String inTenantId,
			String inEndpointId,
			String endpointName,
			String userId,
			ApplicationTranslator<Endpoint, R> translator) {
		TenantId tenantId = TenantId.create(inTenantId);
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}

		EndpointId endpointId = EndpointId.create(inEndpointId);
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		if (optEndpoint.isEmpty()) {
			throw new DomainException("MSG20001");
		}
		
		Endpoint endpoint = optEndpoint.get();
		endpoint.changeEndpointName(endpointName, userId);
		this.endpointRepository.update(endpoint);
		
		return translator.translate(endpoint);
	}
	
	public void delete(
			String inTenantId,
			String inEndpointId) {
		TenantId tenantId = TenantId.create(inTenantId);
		EndpointId endpointId = EndpointId.create(inEndpointId);
		
		//:TODO Check Event
		
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		if (optEndpoint.isEmpty()) {
			throw new DomainException("MSG20001");
		}
		
		this.endpointRepository.remove(optEndpoint.get());
	}
}
