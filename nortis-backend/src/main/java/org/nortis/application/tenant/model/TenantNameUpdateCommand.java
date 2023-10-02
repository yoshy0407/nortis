package org.nortis.application.tenant.model;

import lombok.Builder;

/**
 * テナントを登録するコマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantId テナントID
 * @param name     テナント名
 * @version 1.0.0
 */
@Builder
public record TenantNameUpdateCommand(String tenantId, String name) {

}
