package org.nortis.domain.tenant;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.Paging;

/**
 * テナントのリポジトリインタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface TenantRepository {

    /**
     * テナントIDで取得します
     * 
     * @param tenantId テナントID
     * @return テナントのリスト
     */
    Optional<Tenant> getByTenantId(TenantId tenantId);

    /**
     * テナント識別子で取得します
     * 
     * @param tenantIdentifier テナント識別子
     * @return テナント
     */
    Optional<Tenant> getByTenantIdentifier(TenantIdentifier tenantIdentifier);

    /**
     * 全てのテナントを取得する
     * 
     * @param panging ページング
     * 
     * @return 全てのテナント
     */
    List<Tenant> getTenantPaging(Paging panging);

    /**
     * テナントを保存します
     * 
     * @param tenant テナント
     */
    void save(Tenant tenant);

    /**
     * 更新します
     * 
     * @param tenant テナント
     */
    void update(Tenant tenant);

    /**
     * 削除します
     * 
     * @param tenant テナント
     */
    void remove(Tenant tenant);
}
