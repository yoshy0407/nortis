package org.nortis.infrastructure.doma.repository;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.RoleOperation;
import org.nortis.domain.tenant.RoleOperation_;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.domain.tenant.TenantRole_;
import org.nortis.domain.tenant.Tenant_;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.utils.CollectionConvertUtils;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStarting;
import org.springframework.stereotype.Repository;

/**
 * {@link TenantRepository}のDomaの実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Repository
public class DomaTenantRepository implements TenantRepository {

    private final Entityql entityql;

    private final NativeSql nativeSql;

    private final Tenant_ tenant = new Tenant_();

    private final TenantRole_ tenantRole = new TenantRole_();

    private final RoleOperation_ roleOperation = new RoleOperation_();

    @Override
    public Optional<Tenant> getByTenantId(TenantId tenantId) {
        //@formatter:off
        return select()
                .where(c -> c.eq(tenant.tenantId, tenantId))
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public Optional<Tenant> getByTenantIdentifier(TenantIdentifier tenantIdentifier) {
        //@formatter:off
        return select()
                .where(c -> c.eq(tenant.tenantIdentifier, tenantIdentifier))
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public List<Tenant> getTenantPaging(Paging paging) {
        //@formatter:off
        return select()
                .offset(paging.offset())
                .limit(paging.limit())
                .fetch();
        //@formatter:on
    }

    @Override
    public void save(Tenant tenant) {
        // TENANTのINSERT
        entityql.insert(this.tenant, tenant).execute();
        // TENANT_ROLEのINSERT
        //@formatter:off
        List<TenantRole> tenantRoles = tenant.getRoles().entrySet().stream()
                .map(entry -> entry.getValue())
                .toList();
        entityql.insert(this.tenantRole, tenantRoles).execute();
        //@formatter:on
        // ROLE_OPERATIONのINSERT
        //@formatter:off
        List<RoleOperation> roleOperatons = tenantRoles.stream()
                .flatMap(role -> role.getRoleOperations().entrySet().stream().map(entry -> entry.getValue()))
                .toList();
        entityql.insert(this.roleOperation, roleOperatons).execute();
        //@formatter:on1
    }

    @Override
    public void update(Tenant tenant) {
        // TENANTのINSERT
        entityql.update(this.tenant, tenant).execute();
        // TENANT_ROLEの更新処理
        //@formatter:off
        for (Entry<RoleId, TenantRole> entry : tenant.getRoles().entrySet()) {
            TenantRole tenantRole = entry.getValue();
            //INSERTの場合
            if (tenantRole.isInsert()) {
                entityql.insert(this.tenantRole, tenantRole).execute();
                
                List<RoleOperation> roleOperationList = CollectionConvertUtils.toList(tenantRole.getRoleOperations()); 
                entityql.insert(this.roleOperation, roleOperationList).execute();
            }
            //UPDATEの場合
            if (tenantRole.isUpdate()) {
                entityql.update(this.tenantRole, tenantRole).execute();
                
                for (Entry<OperationId, RoleOperation> operatonEntry : tenantRole.getRoleOperations().entrySet()) {
                    RoleOperation roleOperation = operatonEntry.getValue();
                    //イミュータブルなエンティティなので、データは追加か削除しかない
                    //INSERT
                    if (roleOperation.isInsert()) {
                        entityql.insert(this.roleOperation, roleOperation).execute();
                    }
                    //DELETE
                    if (roleOperation.isDelete()) {
                        entityql.delete(this.roleOperation, roleOperation).execute();                            
                    }
                    
                }
            }
            //DELETEの場合
            if (tenantRole.isDelete()) {
                entityql.delete(this.tenantRole, tenantRole).execute();     

                List<RoleOperation> roleOperatons = CollectionConvertUtils.toList(tenantRole.getRoleOperations()); 
                entityql.delete(this.roleOperation, roleOperatons).execute();
            }
        }
    }

    @Override
    public void remove(Tenant tenant) {
        //@formatter:off
        List<RoleId> roleIds = tenant.getRoles().entrySet().stream()
                .map(entry -> entry.getKey())
                .toList();
        //@formatter:on
        // ROLE_OPERATONの削除
        //@formatter:off
        nativeSql.delete(roleOperation)
                .where(c -> c.in(roleOperation.roleId, roleIds))
                .execute();
        //@formatter:on
        // TENANT_ROLEの削除
        //@formatter:off
        nativeSql.delete(tenantRole)
                .where(c -> c.in(tenantRole.roleId, roleIds))
                .execute();
        //@formatter:on

        // TENANTの削除
        entityql.delete(this.tenant, tenant).execute();
    }

    private EntityqlSelectStarting<Tenant> select() {
        //@formatter:off
        return entityql.from(tenant)
                .leftJoin(tenantRole, on -> on.eq(tenant.tenantId, tenantRole.tenantId))
                .leftJoin(roleOperation, on -> on.eq(tenantRole.roleId, roleOperation.roleId))
                .associate(tenantRole, roleOperation, (role, operation) -> role.getRoleOperations().put(operation.getOperationId(), operation))
                .associate(tenant, tenantRole, (t, role) -> t.getRoles().put(role.getRoleId(), role));
        //@formatter:on
    }

}
