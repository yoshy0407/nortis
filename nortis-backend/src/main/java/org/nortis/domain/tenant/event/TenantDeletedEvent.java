package org.nortis.domain.tenant.event;

import org.nortis.domain.tenant.value.TenantId;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TenantDeletedEvent {

	private final TenantId tenantId;

}
