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

/**
 * エンドポイント
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "ENDPOINT")
@Entity
public class Endpoint extends AbstractAggregateRoot<Endpoint> {

	/**
	 * UUID
	 */
	@Id
	@Column(name = "UUID")
	private UUID uuid;
	
	/**
	 * エンドポイントID
	 */
	@Column(name = "ENDPOINT_ID")
	private EndpointId endpointId;
	
	/**
	 * テナントID
	 */
	@Column(name = "TENANT_ID")
	private TenantId tenantId;
	
	/**
	 * エンドポイント名
	 */
	@Column(name = "ENDPOINT_NAME")
	private String endpointName;

}
