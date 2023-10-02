package org.nortis.domain.tenant;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.doma.EntityOperation;
import org.nortis.infrastructure.doma.NortisEntityListener;
import org.nortis.infrastructure.doma.entity.AbstractEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * テナントのロールマスタ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "TENANT_ROLE")
@Entity(listener = NortisEntityListener.class, metamodel = @Metamodel)
public class TenantRole extends AbstractEntity {

    @Id
    @Column(name = "TENANT_ID")
    private TenantId tenantId;

    @Id
    @Column(name = "ROLE_ID")
    private RoleId roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Transient
    private Map<OperationId, RoleOperation> roleOperations = new LinkedHashMap<>();

    /**
     * テナントIDを設定します
     * 
     * @param tenantId テナントID
     * @throws DomainException ビジネスロジックエラー
     */
    public void setTenantId(TenantId tenantId) throws DomainException {
        Validations.notNull(tenantId, "テナントID");
        this.tenantId = tenantId;
    }

    /**
     * ロールIDを設定します
     * 
     * @param roleId ロールID
     * @throws DomainException ビジネスロジックエラー
     */
    public void setRoleId(RoleId roleId) throws DomainException {
        Validations.notNull(roleId, "ロールID");
        this.roleId = roleId;
    }

    /**
     * ロール名
     * 
     * @param roleName ロール名
     * @throws DomainException ビジネスロジックエラー
     */
    public void setRoleName(String roleName) throws DomainException {
        Validations.hasText(roleName, "ロール名");
        Validations.maxTextLength(roleName, 20, "ロール名");
        this.roleName = roleName;
    }

    /**
     * ロールの権限を付与します
     * 
     * @param authority 権限
     */
    public void grantAuthority(OperationAuthority authority) {
        for (OperationId operationId : authority.getOperationIds()) {
            RoleOperation roleOperaion = new RoleOperation(this.roleId, operationId,
                    SecurityContextUtils.getCurrentAuthorizedId().toString(), LocalDateTime.now());
            roleOperaion.setInsert();
            this.roleOperations.put(operationId, roleOperaion);
        }
    }

    /**
     * ロールの権限を外します
     * 
     * @param authority 権限
     */
    public void revokeAuthority(OperationAuthority authority) {
        for (OperationId operationId : authority.getOperationIds()) {
            if (this.roleOperations.containsKey(operationId)) {
                this.roleOperations.get(operationId).setDelete();
            }
        }
    }

    /**
     * 削除します
     */
    public void delete() {
        for (Entry<OperationId, RoleOperation> entry : this.roleOperations.entrySet()) {
            entry.getValue().setDelete();
        }
        this.setDelete();
    }

    /**
     * このロールでオペレーション可能か確認します
     * 
     * @param operationId オペレーションID
     * @return 可能かどうか
     */
    public boolean canOperationOf(OperationId operationId) {
        RoleOperation roleOperation = this.roleOperations.get(operationId);
        // なければ権限なしと判断
        if (roleOperation == null) {
            return false;
        }
        // エンティティが削除とマークされている場合、すでに削除されている状態のため、
        // 権限なしにする
        return !roleOperation.getEntityOperation().equals(EntityOperation.DELETE);
    }

    /**
     * テナントロールを作成します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @param roleName ロール名
     * @return テナントロール
     * @throws DomainException ドメインロジックエラー
     */
    public static TenantRole create(TenantId tenantId, RoleId roleId, String roleName) throws DomainException {
        TenantRole tenantRole = new TenantRole();
        tenantRole.setTenantId(tenantId);
        tenantRole.setRoleId(roleId);
        tenantRole.setRoleName(roleName);
        return tenantRole;
    }

}