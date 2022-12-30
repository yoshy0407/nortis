package org.nortis.domain.tenant;


import java.util.Optional;

import org.nortis.domain.tenant.value.TenantId;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * テナントのリポジトリインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ConfigAutowireable
@Dao
public interface TenantRepository  {

	/**
	 * テナントIDで取得します
	 * @param tenantId テナントID
	 * @return テナントのリスト
	 */
	@Select
	Optional<Tenant> get(TenantId tenantId);
	
	@Insert
	int save(Tenant tenant);

	@Update
	int update(Tenant tenant);

	@Delete
	int remove(Tenant tenant);

}
