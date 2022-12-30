package org.nortis.domain.mail;

import java.util.Optional;

import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@DomainService
public class MailConsumerDomainService {

	private final EndpointRepository endpointRepository;
	
	public MailConsumer createMailConsumer(TenantId tenantId, EndpointId endpointId, MailAddress mailAddress, String userId) {		
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		
		if (optEndpoint.isEmpty()) {
			throw new DomainException("MSG20001");
		}
		
		return MailConsumer.create(endpointId, mailAddress, userId);
	}
}
