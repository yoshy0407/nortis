package org.nortis.domain.endpoint;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.application.Paging;

/**
 * エンドポイントのリポジトリインタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface EndpointRepository {

    /**
     * 1件検索
     * 
     * @param tenantId   テナントID
     * @param endpointId エンドポイントID
     * @return 検索結果
     */
    Optional<Endpoint> get(TenantId tenantId, EndpointId endpointId);

    /**
     * エンドポイント識別子から取得します
     * 
     * @param tenantId           テナントID
     * @param endpointIdentifier エンドポイント識別子
     * @return 取得結果
     */
    Optional<Endpoint> getByEndpointIdentifier(TenantId tenantId, EndpointIdentifier endpointIdentifier);

    /**
     * テナントIDからエンドポイントを取得します
     * 
     * @param tenantId テナントID
     * @return エンドポイントのリスト
     */
    List<Endpoint> getFromTenantId(TenantId tenantId);

    /**
     * ページングでエンドポイントを取得します
     * 
     * @param tenantId テナントID
     * @param paging   ページング
     * @return リスト
     */
    List<Endpoint> getList(TenantId tenantId, Paging paging);

    /**
     * 保存します
     * 
     * @param endpoint 保存するオブジェクト
     */
    void save(Endpoint endpoint);

    /**
     * 更新します
     * 
     * @param endpoint 更新するオブジェクト
     */
    void update(Endpoint endpoint);

    /**
     * オブジェクトを削除します
     * 
     * @param endpoint 削除するオブジェクト
     */
    void remove(Endpoint endpoint);

    /**
     * 複数オブジェクトを削除します
     * 
     * @param endpointList 削除するオブジェクトのリスト
     */
    void removeAll(List<Endpoint> endpointList);

}
