package org.nortis.domain.event;

import java.util.UUID;

import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.validation.Validations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * 受信イベント
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "RECEIVE_EVENT")
@Entity
public class ReceiveEvent {

	@Id
	@Column(name = "EVENT_ID")
	private UUID eventId;
	
	@Column(name = "TENANT_ID")
	private TenantId tenantId;
	
	/**
	 * イベントIDを設定します
	 * @param eventId イベントID
	 */
	public void setEventId(UUID eventId) {
		Validations.notNull(eventId, "イベントID");
		this.eventId = eventId;
	}

	/**
	 * テナントIDを設定します
	 * @param tenantId テナントID
	 */
	public void setTenantId(TenantId tenantId) {
		Validations.notNull(eventId, "テナントID");
		this.tenantId = tenantId;
	}
	
}
