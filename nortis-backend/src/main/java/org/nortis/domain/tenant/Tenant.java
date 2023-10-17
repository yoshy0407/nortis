package org.nortis.domain.tenant;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.ToString;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.domain.user.Suser;
import org.nortis.infrastructure.doma.EntityOperation;
import org.nortis.infrastructure.doma.entity.RootEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * テナント
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "TENANT")
@Entity(listener = TenantEntityListener.class, metamodel = @Metamodel)
public class Tenant extends RootEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * テナントID
     */
    @Id
    @Column(name = "TENANT_ID")
    private TenantId tenantId;

    /**
     * テナント名
     */
    @Column(name = "TENANT_NAME")
    private String tenantName;

    @Column(name = "TENANT_IDENTIFIER")
    private TenantIdentifier tenantIdentifier;

    @Transient
    private LinkedHashMap<RoleId, TenantRole> roles = new LinkedHashMap<>();

    /**
     * ロールを返却します
     * 
     * @param roleId ロールID
     * @return ロール
     */
    public Optional<TenantRole> getRole(RoleId roleId) {
        return Optional.ofNullable(this.roles.get(roleId));
    }

    /**
     * テナントIDを設定します
     * 
     * @param tenantId テナントID
     * @throws DomainException ドメインロジックエラー
     */
    public void setTenantId(TenantId tenantId) throws DomainException {
        Validations.notNull(tenantId, "テナントID");
        this.tenantId = tenantId;
    }

    /**
     * テナント名を設定します
     * 
     * @param tenantName テナント名
     * @throws DomainException ドメインロジックエラー
     */
    public void setTenantName(String tenantName) throws DomainException {
        Validations.hasText(tenantName, "テナント名");
        Validations.maxTextLength(tenantName, 50, "テナント名");
        this.tenantName = tenantName;
    }

    /**
     * テナント識別子を設定します
     * 
     * @param tenantIdentifier テナント識別子
     * @throws DomainException ドメインロジックエラー
     */
    public void setTenantIdentifier(TenantIdentifier tenantIdentifier) throws DomainException {
        Validations.notNull(tenantId, "テナント識別子");
        this.tenantIdentifier = tenantIdentifier;
    }

    /**
     * テナント名を変更します
     * 
     * @param tenantName テナント名
     * @throws DomainException ドメインロジックエラー
     */
    public void changeTenantName(String tenantName) throws DomainException {
        setTenantName(tenantName);
    }

    /**
     * エンドポイントを作成します
     * 
     * @param endpointId         エンドポイント
     * @param endpointIdentifier エンドポイント識別子
     * @param endpointName       エンドポイント名
     * @return エンドポイント
     * @throws DomainException ドメインロジックエラー
     */
    public Endpoint createEndpoint(EndpointId endpointId, EndpointIdentifier endpointIdentifier, String endpointName)
            throws DomainException {
        return Endpoint.create(this.tenantId, endpointId, endpointIdentifier, endpointName);
    }

    /**
     * コンシューマを作成します
     * 
     * @param consuerId        コンシューマID
     * @param consumerName     コンシューマ名
     * @param consumerTypeCode コンシューマタイプコード
     * @param textType         テキストタイプ
     * @param parameter        パラメータ
     * @return コンシューマ
     * @throws DomainException ドメインロジックエラー
     */
    public Consumer createConsumer(ConsumerId consuerId, String consumerName, String consumerTypeCode,
            TextType textType, Map<String, String> parameter) throws DomainException {
        return Consumer.create(this.tenantId, consuerId, consumerName, consumerTypeCode, textType, parameter);
    }

    /**
     * ロールを作成します
     * 
     * @param roleId        ロールID
     * @param roleName      ロール名
     * @param authorityList 権限リスト
     * @throws DomainException ビジネスロジックエラー
     */
    public void createRole(RoleId roleId, String roleName, List<OperationAuthority> authorityList)
            throws DomainException {
        TenantRole tenantRole = TenantRole.create(this.tenantId, roleId, roleName);
        tenantRole.setInsert();
        for (OperationAuthority operationAuthority : authorityList) {
            tenantRole.grantAuthority(operationAuthority);
        }
        this.roles.put(roleId, tenantRole);
    }

    /**
     * ロール名を変更します
     * 
     * @param roleId   ロールID
     * @param roleName ロール名
     * @throws DomainException ビジネスロジックエラー
     */
    public void changeRoleNameOf(RoleId roleId, String roleName) throws DomainException {
        TenantRole tenantRole = this.roles.get(roleId);
        if (tenantRole != null) {
            tenantRole.setRoleName(roleName);
        }
    }

    /**
     * ロールに対する権限を追加します
     * 
     * @param roleId    ロールID
     * @param authority 権限
     */
    public void grantRoleAuthority(RoleId roleId, OperationAuthority authority) {
        this.roles.get(roleId).grantAuthority(authority);
    }

    /**
     * ロールに対する権限を削除します
     * 
     * @param roleId    ロールID
     * @param authority 権限
     */
    public void revokeRoleAuthority(RoleId roleId, OperationAuthority authority) {
        this.roles.get(roleId).revokeAuthority(authority);
    }

    /**
     * ロールを削除します
     * 
     * @param roleId ロールID
     */
    public void deleteRole(RoleId roleId) {
        TenantRole role = this.roles.get(roleId);
        role.delete();
    }

    /**
     * ユーザの操作を許可します
     * 
     * @param user      ユーザ
     * @param operation 操作内容
     * @return 許可するかどうか
     */
    public boolean permitOperation(Suser user, OperationId operation) {
        List<RoleId> roleIds = user.getHasRoleOf(tenantId);
        for (RoleId roleId : roleIds) {
            TenantRole role = this.roles.get(roleId);
            if (role == null) {
                continue;
            }
            // すでに削除済みの場合は対象としない
            if (role.getEntityOperation().equals(EntityOperation.DELETE)) {
                continue;
            }
            if (role.canOperationOf(operation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * APIキーを作成します
     * 
     * @return 認証
     * @throws DomainException ドメインロジックエラー
     */
    public Authentication createApiKey() throws DomainException {
        return Authentication.createFromTenant(this.tenantId);
    }

    /**
     * エンティティを新規作成します
     * 
     * @param tenantId         テナントID
     * @param tenantIdentifier テナント識別子
     * @param tenantName       テナント名
     * @return テナント
     * @throws DomainException ドメインロジックエラー
     */
    public static Tenant create(TenantId tenantId, TenantIdentifier tenantIdentifier, String tenantName)
            throws DomainException {
        final Tenant entity = new Tenant();
        entity.setTenantId(tenantId);
        entity.setTenantIdentifier(tenantIdentifier);
        entity.setTenantName(tenantName);
        return entity;
    }

}
