package org.nortis.domain.tenant;

import java.util.Optional;

import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import lombok.AllArgsConstructor;

/**
 * テナントに関するドメインサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@DomainService
@AllArgsConstructor
public class TenantDomainService {

	/**
	 * テナントリポジトリ
	 */
	private final TenantRepository tenantRepository;

	/**
	 * 作成するテナントを返します
	 * @param tenantId テナント省略名
	 * @param tenantName テナント名
	 * @param createId 作成者ID
	 * @return テナント
	 */
	public Tenant createTenant(TenantId tenantId, String tenantName, String createId) {
		
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isPresent()) {
			throw new DomainException("MSG10001");
		}
		
		return Tenant.create(tenantId, tenantName, createId);
	}
	


}
