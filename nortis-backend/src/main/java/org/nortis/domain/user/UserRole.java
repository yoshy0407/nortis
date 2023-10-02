package org.nortis.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.doma.entity.ImmutableEntity;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 * テナントユーザエンティティ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "USER_ROLE")
public class UserRole extends ImmutableEntity {

    @Id
    @Column(name = "USER_ID")
    private final UserId userId;

    @Id
    @Column(name = "TENANT_ID")
    private final TenantId tenantId;

    @Column(name = "ROLE_ID")
    private final RoleId roleId;

    /**
     * インスタンスを生成します
     * 
     * @param userId   ユーザID
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @param createId 作成者
     * @param createDt 作成日時
     */
    public UserRole(UserId userId, TenantId tenantId, RoleId roleId, String createId, LocalDateTime createDt) {
        super(createId, createDt);
        this.userId = userId;
        this.tenantId = tenantId;
        this.roleId = roleId;
    }

}
