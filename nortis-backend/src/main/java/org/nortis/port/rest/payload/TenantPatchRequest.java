package org.nortis.port.rest.payload;

/**
 * テナント更新リクエスト
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param name 更新する名前
 */
public record TenantPatchRequest(
		String name) {

}
