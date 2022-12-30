package org.nortis.domain.endpoint.event;

import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EndpointDeletedEvent {

	private final TenantId tenantId;
	
	private final EndpointId endpointId;
}
