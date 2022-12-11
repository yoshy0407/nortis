package org.nortis.domain.event;

import java.util.UUID;

import org.nortis.domain.tenant.value.TenantId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "RECEIVE_EVENT")
@Entity
public class ReceiveEvent {

	@Id
	@Column(name = "EVENT_ID")
	private UUID eventId;
	
	private TenantId tenantId;
}
