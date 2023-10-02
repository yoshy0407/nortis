package org.nortis.domain.authentication;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 認証リポジトリ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ConfigAutowireable
@Dao
public interface AuthenticationRepository {

    /**
     * APIキーから取得します
     * 
     * @param apiKey APIキー
     * @return 認証
     */
    @Select
    Optional<Authentication> get(ApiKey apiKey);

    /**
     * テナントIDから取得します
     * 
     * @param tenantId テナントID
     * @return 認証
     */
    @Select
    Optional<Authentication> getFromTenantId(TenantId tenantId);

    /**
     * ユーザIDから取得します
     * 
     * @param userId ユーザID
     * @return 認証
     */
    @Select
    Optional<Authentication> getFromUserId(UserId userId);

    /**
     * ユーザの認証情報を全て取得します
     * 
     * @return 認証
     */
    @Select
    List<Authentication> getUserAuthentication();

    /**
     * 保存します
     * 
     * @param authentication 認証
     * @return 登録件数
     */
    @Insert
    int save(Authentication authentication);

    /**
     * 更新します
     * 
     * @param authentication 認証
     * @return 更新件数
     */
    @Update
    int update(Authentication authentication);

    /**
     * 削除します
     * 
     * @param authentication 認証
     * @return 削除件数
     */
    @Delete
    int remove(Authentication authentication);

}
