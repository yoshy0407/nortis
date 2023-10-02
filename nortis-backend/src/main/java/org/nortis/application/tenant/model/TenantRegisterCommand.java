package org.nortis.application.tenant.model;

import lombok.Builder;

/**
 * テナントを登録するコマンドです
 * 
 * @author yoshiokahiroshi
 * @param tenantIdentifier テナント識別子
 * @param name             テナント名
 * @version 1.0.0
 */
@Builder
public record TenantRegisterCommand(String tenantIdentifier, String name) {

}
