package org.nortis.domain.tenant.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nortis.domain.tenant.value.TenantId;


/**
 * テナントを削除したことを表すイベントです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public class TenantDeletedEvent {

	/** テナントID */
	private final TenantId tenantId;

}
