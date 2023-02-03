package org.nortis.port.tenant;

/**
 * テナント更新リクエスト
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param name 更新する名前
 */
public record TenantPatchRequest(
		String name) {

}
