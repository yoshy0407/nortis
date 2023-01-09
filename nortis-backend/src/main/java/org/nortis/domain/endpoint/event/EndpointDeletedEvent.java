package org.nortis.domain.endpoint.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;

/**
 * エンドポイントが削除されたことを表すイベントです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public class EndpointDeletedEvent {

	/** テナントID */
	private final TenantId tenantId;
	
	/** エンドポイントID */
	private final EndpointId endpointId;
	
	/** 更新者ID */
	private final String updateUserId;
}
