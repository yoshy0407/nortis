package org.nortis.domain.authentication;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;

/**
 * 認証リポジトリ
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ConfigAutowireable
@Dao
public interface AuthenticationRepository {

	/**
	 * APIキーから取得します
	 * @param apiKey APIキー 
	 * @return 認証
	 */
	@Select
	Optional<Authentication> get(ApiKey apiKey);
	
	/**
	 * テナントIDから取得します
	 * @param tenantId テナントID
	 * @return 認証
	 */
	@Select
	Optional<Authentication> getFromTenantId(TenantId tenantId);

	/**
	 * ユーザIDから取得します
	 * @param userId ユーザID
	 * @return 認証
	 */
	@Select
	Optional<Authentication> getFromUserId(UserId userId);
	
	/**
	 * ユーザの認証情報を全て取得します
	 * @return 認証
	 */
	@Select
	List<Authentication> getUserAuthentication();
	
	/**
	 * 保存します
	 * @param authentication 認証
	 */
	default void save(Authentication authentication) {
		Entityql entityql = new Entityql(Config.get(this));
		
		Authentication_ auth = new Authentication_();
		entityql.insert(auth, authentication).execute();
	}

	/**
	 * 更新します
	 * @param authentication 認証
	 */
	default void update(Authentication authentication) {
		Entityql entityql = new Entityql(Config.get(this));
		Authentication_ auth = new Authentication_();
		entityql.update(auth, authentication).execute();
	}
	
	/**
	 * 削除します
	 * @param authentication 認証
	 */
	default void remove(Authentication authentication) {
		Entityql entityql = new Entityql(Config.get(this));
		
		Authentication_ auth = new Authentication_();
		entityql.delete(auth, authentication).execute();
		
	}
}
