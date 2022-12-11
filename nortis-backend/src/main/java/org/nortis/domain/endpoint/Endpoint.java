package org.nortis.domain.endpoint;

import java.util.UUID;

import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "ENDPOINT")
@Entity
public class Endpoint extends AbstractAggregateRoot<Endpoint> {

	@Id
	@Column(name = "UUID")
	private UUID uuid;
	
	@Column(name = "ENDPOINT_ID")
	private EndpointId endpointId;
	
	@Column(name = "TENANT_ID")
	private TenantId tenantId;
	
	@Column(name = "ENDPOINT_NAME")
	private String endpointName;

}
