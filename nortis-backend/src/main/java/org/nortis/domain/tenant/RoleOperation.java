package org.nortis.domain.tenant;

import java.time.LocalDateTime;
import lombok.Getter;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.infrastructure.doma.entity.ImmutableEntity;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 * ロールオペレーションのエンティティクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Table(name = "ROLE_OPERATION")
@Getter
@Entity(immutable = true, metamodel = @Metamodel)
public class RoleOperation extends ImmutableEntity {

    @Id
    @Column(name = "ROLE_ID")
    private final RoleId roleId;

    @Id
    @Column(name = "OPERATION_ID")
    private final OperationId operationId;

    /**
     * インスタンスを生成します
     * 
     * @param roleId      ロールID
     * @param operationId オペレーションID
     * @param createId    作成者
     * @param createDt    作成日
     */
    public RoleOperation(RoleId roleId, OperationId operationId, String createId, LocalDateTime createDt) {
        super(createId, createDt);
        this.roleId = roleId;
        this.operationId = operationId;
    }

}
