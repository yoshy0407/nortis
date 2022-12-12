package org.nortis.domain.tenant;

import java.util.List;
import java.util.UUID;

import org.nortis.domain.tenant.value.TenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * テナントのリポジトリインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

	/**
	 * テナントIDで取得します
	 * @param tenantId テナントID
	 * @return テナントのリスト
	 */
	List<Tenant> findByTenantId(TenantId tenantId);

}
