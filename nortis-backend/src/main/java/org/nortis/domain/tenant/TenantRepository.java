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
	
	/**
	 * テナントを保存します
	 * @param tenant テナント
	 * @return 保存件数
	 */
	@Insert
	int save(Tenant tenant);

	/**
	 * 更新します
	 * @param tenant テナント
	 * @return 更新件数
	 */
	@Update
	int update(Tenant tenant);

	/**
	 * 削除します
	 * @param tenant テナント
	 * @return 削除件数
	 */
	@Delete
	int remove(Tenant tenant);

}
