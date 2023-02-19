package org.nortis.domain.tenant;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

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
	 * @throws DomainException ドメインロジックエラー
	 */
	public Tenant createTenant(TenantId tenantId, String tenantName, String createId) throws DomainException {
		
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isPresent()) {
			throw new DomainException(MessageCodes.nortis10001());
		}
		
		return Tenant.create(tenantId, tenantName, createId);
	}
	
	/**
	 * テナントの存在チェックを実施します
	 * @param tenantId テナントID
	 * @return テナントの存在確認
	 */
	public boolean existTenant(TenantId tenantId) {
		return this.tenantRepository.get(tenantId).isPresent();
	}

}
