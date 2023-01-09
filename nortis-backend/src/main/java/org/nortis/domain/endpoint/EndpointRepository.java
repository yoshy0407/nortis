package org.nortis.domain.endpoint;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * エンドポイントのリポジトリインタフェースです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ConfigAutowireable
@Dao
public interface EndpointRepository {

	/**
	 * 1件検索
	 * @param tenantId テナントID
	 * @param endpointId エンドポイントID
	 * @return 検索結果
	 */
	@Select
	Optional<Endpoint> get(TenantId tenantId, EndpointId endpointId);
	
	/**
	 * テナントIDからエンドポイントを取得します
	 * @param tenantId テナントID
	 * @return エンドポイントのリスト
	 */
	@Select
	List<Endpoint> getFromTenantId(TenantId tenantId);
	
	/**
	 * 保存します
	 * @param endpoint 保存するオブジェクト
	 * @return 更新件数
	 */
	@Insert
	int save(Endpoint endpoint);

	/**
	 * 更新します
	 * @param endpoint 更新するオブジェクト
	 * @return 更新件数
	 */
	@Update
	int update(Endpoint endpoint);

	/**
	 * オブジェクトを削除します
	 * @param endpoint 削除するオブジェクト
	 * @return 削除件数
	 */
	@Delete
	int remove(Endpoint endpoint);

	/**
	 * 複数オブジェクトを削除します
	 * @param endpointList 削除するオブジェクトのリスト
	 * @return 削除結果
	 */
	@BatchDelete
	int[] removeAll(List<Endpoint> endpointList);

}
