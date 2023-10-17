package org.nortis.infrastructure.doma.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.domain.tenant.RoleOperation;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.user.TestUsers;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
      "/META-INF/ddl/dropTenant.sql",
      "/ddl/createTenant.sql",
      "/META-INF/data/domain/del_ins_tenant.sql"
})
//@formatter:on
@ContextConfiguration(classes = DomaTenantRepositoryTestConfig.class)
@RecordApplicationEvents
class DomaTenantRepositoryTest extends RepositoryTestBase {

    @Autowired
    Entityql entityql;

    @Autowired
    NativeSql nativeSql;

    TenantRepository repository;

    @BeforeEach
    void setup() {
        this.repository = new DomaTenantRepository(entityql, nativeSql);
    }

    @Test
    void testGetByTenantId() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        Optional<Tenant> optTenant = this.repository.getByTenantId(tenantId);

        assertThat(optTenant).isPresent();

        Tenant tenant = optTenant.get();
        assertTenant(tenant, tenantId, TenantIdentifier.create("TEST1"), "テストテナント１");

        LinkedHashMap<RoleId, TenantRole> roleMap = tenant.getRoles();
        assertThat(roleMap).hasSize(2);
        var roleId1 = RoleId.create("00001");
        var tenantRole1 = roleMap.get(roleId1);
        assertTenantRole(tenantRole1, tenantId, roleId1, "エンドポイント管理ロール");

        Map<OperationId, RoleOperation> roleOperationMap1 = tenantRole1.getRoleOperations();
        assertThat(roleOperationMap1).hasSize(2);
        assertRoleOperation(roleOperationMap1.get(OperationId.READ_ENDPOINT), roleId1, OperationId.READ_ENDPOINT);
        assertRoleOperation(roleOperationMap1.get(OperationId.WRITE_ENDPOINT), roleId1, OperationId.WRITE_ENDPOINT);

        var roleId2 = RoleId.create("00002");
        var tenantRole2 = roleMap.get(roleId2);
        assertTenantRole(tenantRole2, tenantId, roleId2, "API作成ロール");

        Map<OperationId, RoleOperation> roleOperationMap2 = tenantRole2.getRoleOperations();
        assertThat(roleOperationMap2).hasSize(1);
        assertRoleOperation(roleOperationMap2.get(OperationId.WRITE_APIKEY), roleId2, OperationId.WRITE_APIKEY);

    }

    @Test
    void testGetByTenantIdentifier() throws DomainException {
        var tenantId = TenantId.create("0000000001");
        var tenantIdentifier = TenantIdentifier.create("TEST1");
        Optional<Tenant> optTenant = this.repository.getByTenantIdentifier(tenantIdentifier);

        assertThat(optTenant).isPresent();

        Tenant tenant = optTenant.get();
        assertTenant(tenant, tenantId, tenantIdentifier, "テストテナント１");

        LinkedHashMap<RoleId, TenantRole> roleMap = tenant.getRoles();
        assertThat(roleMap).hasSize(2);
        var roleId1 = RoleId.create("00001");
        var tenantRole1 = roleMap.get(roleId1);
        assertTenantRole(tenantRole1, tenantId, roleId1, "エンドポイント管理ロール");

        Map<OperationId, RoleOperation> roleOperationMap1 = tenantRole1.getRoleOperations();
        assertThat(roleOperationMap1).hasSize(2);
        assertRoleOperation(roleOperationMap1.get(OperationId.READ_ENDPOINT), roleId1, OperationId.READ_ENDPOINT);
        assertRoleOperation(roleOperationMap1.get(OperationId.WRITE_ENDPOINT), roleId1, OperationId.WRITE_ENDPOINT);

        var roleId2 = RoleId.create("00002");
        var tenantRole2 = roleMap.get(roleId2);
        assertTenantRole(tenantRole2, tenantId, roleId2, "API作成ロール");

        Map<OperationId, RoleOperation> roleOperationMap2 = tenantRole2.getRoleOperations();
        assertThat(roleOperationMap2).hasSize(1);
        assertRoleOperation(roleOperationMap2.get(OperationId.WRITE_APIKEY), roleId2, OperationId.WRITE_APIKEY);

    }

    @Test
    void testSave() throws DomainException {
        var tenantId = TenantId.createNew(100);
        var tenantIdentifier = TenantIdentifier.create("INSERT");
        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, "テストSAVE");

        var roleId = RoleId.createNew(10);
        tenant.createRole(roleId, "テストロール", Lists.list(OperationAuthority.WRITE_APIKEY));

        this.repository.save(tenant);

        Optional<Tenant> optTenant2 = this.repository.getByTenantId(tenantId);

        assertThat(optTenant2).isPresent();

        Tenant tenantResult = optTenant2.get();
        assertTenant(tenantResult, tenantId, tenantIdentifier, "テストSAVE");

        LinkedHashMap<RoleId, TenantRole> roleMap = tenantResult.getRoles();
        assertThat(roleMap).hasSize(1);
        var tenantRole1 = roleMap.get(roleId);
        assertTenantRole(tenantRole1, tenantId, roleId, "テストロール");

        Map<OperationId, RoleOperation> roleOperationMap1 = tenantRole1.getRoleOperations();
        assertThat(roleOperationMap1).hasSize(1);
        assertRoleOperation(roleOperationMap1.get(OperationId.WRITE_APIKEY), roleId, OperationId.WRITE_APIKEY);
    }

    @Test
    void testUpdate() throws DomainException {
        TestUsers.adminUser().setSecurityContext();
        var tenantId = TenantId.create("0000000001");
        var addRoleId = RoleId.create("01000");
        var delRoleId = RoleId.create("00002");
        var updRoleId = RoleId.create("00001");
        Optional<Tenant> optTenant = this.repository.getByTenantId(tenantId);

        Tenant tenant = optTenant.get();
        // TENANT の更新
        tenant.changeTenantName("テストテナント更新");
        // TENANT_ROLEの追加
        tenant.createRole(addRoleId, "テストロール", Lists.list(OperationAuthority.WRITE_TENANT_NAME));
        // TENANT_ROLEの更新
        tenant.changeRoleNameOf(updRoleId, "更新ロール");
        // TENANT_ROLEの削除
        tenant.deleteRole(delRoleId);

        // ROLE_OPERATIONの追加
        tenant.grantRoleAuthority(updRoleId, OperationAuthority.WRITE_TENANT_NAME);
        // ROLE_OPERATIONの削除
        tenant.revokeRoleAuthority(updRoleId, OperationAuthority.READWRITE_ENDPOINT);

        // テスト
        this.repository.update(tenant);

        // 結果確認
        Optional<Tenant> optTenant2 = this.repository.getByTenantId(tenantId);
        Tenant tenant2 = optTenant2.get();

        assertUpdateTenant(tenant2, tenantId, TenantIdentifier.create("TEST1"), "テストテナント更新");

        LinkedHashMap<RoleId, TenantRole> roleMap = tenant2.getRoles();
        assertThat(roleMap).hasSize(2);

        assertThat(roleMap.containsKey(delRoleId)).isFalse();

        var tenantRole1 = roleMap.get(addRoleId);
        assertTenantRole(tenantRole1, tenantId, addRoleId, "テストロール");

        Map<OperationId, RoleOperation> roleOperationMap1 = tenantRole1.getRoleOperations();
        assertThat(roleOperationMap1).hasSize(1);
        assertRoleOperation(roleOperationMap1.get(OperationId.WRITE_TENANT_NAME), addRoleId,
                OperationId.WRITE_TENANT_NAME);

        var tenantRole2 = roleMap.get(updRoleId);
        assertUpdateTenantRole(tenantRole2, tenantId, updRoleId, "更新ロール");

        Map<OperationId, RoleOperation> roleOperationMap2 = tenantRole2.getRoleOperations();
        assertThat(roleOperationMap2).hasSize(1);
        assertRoleOperation(roleOperationMap2.get(OperationId.WRITE_TENANT_NAME), updRoleId,
                OperationId.WRITE_TENANT_NAME);

    }

    // イベントのところが悩ましい
    @Test
    void testRemove() throws DomainException {
        TestUsers.memberUser().setSecurityContext();

        var tenantId = TenantId.create("0000000001");
        Optional<Tenant> optTenant = this.repository.getByTenantId(tenantId);
        this.repository.remove(optTenant.get());

        Optional<Tenant> optTenant2 = this.repository.getByTenantId(tenantId);
        assertThat(optTenant2).isEmpty();
    }

    private void assertTenant(Tenant actual, TenantId tenantId, TenantIdentifier tenantIdentifier, String tenantName) {
        assertThat(actual.getTenantId()).isEqualTo(tenantId);
        assertThat(actual.getTenantIdentifier()).isEqualTo(tenantIdentifier);
        assertThat(actual.getTenantName()).isEqualTo(tenantName);
        assertThat(actual.getCreateDt()).isNotNull();
        assertThat(actual.getCreateId()).isNotNull();
        assertThat(actual.getUpdateDt()).isNull();
        assertThat(actual.getUpdateId()).isNull();
        assertThat(actual.getVersion()).isEqualTo(1);
    }

    private void assertUpdateTenant(Tenant actual, TenantId tenantId, TenantIdentifier tenantIdentifier,
            String tenantName) {
        assertThat(actual.getTenantId()).isEqualTo(tenantId);
        assertThat(actual.getTenantIdentifier()).isEqualTo(tenantIdentifier);
        assertThat(actual.getTenantName()).isEqualTo(tenantName);
        assertThat(actual.getCreateDt()).isNotNull();
        assertThat(actual.getCreateId()).isNotNull();
        assertThat(actual.getUpdateDt()).isNotNull();
        assertThat(actual.getUpdateId()).isNotNull();
        assertThat(actual.getVersion()).isEqualTo(2);
    }

    private void assertTenantRole(TenantRole actual, TenantId tenantId, RoleId roleId, String roleName) {
        assertThat(actual.getTenantId()).isEqualTo(tenantId);
        assertThat(actual.getRoleId()).isEqualTo(roleId);
        assertThat(actual.getRoleName()).isEqualTo(roleName);
        assertThat(actual.getCreateDt()).isNotNull();
        assertThat(actual.getCreateId()).isNotNull();
        assertThat(actual.getUpdateDt()).isNull();
        assertThat(actual.getUpdateId()).isNull();
        assertThat(actual.getVersion()).isEqualTo(1);
    }

    private void assertUpdateTenantRole(TenantRole actual, TenantId tenantId, RoleId roleId, String roleName) {
        assertThat(actual.getTenantId()).isEqualTo(tenantId);
        assertThat(actual.getRoleId()).isEqualTo(roleId);
        assertThat(actual.getRoleName()).isEqualTo(roleName);
        assertThat(actual.getCreateDt()).isNotNull();
        assertThat(actual.getCreateId()).isNotNull();
        assertThat(actual.getUpdateDt()).isNotNull();
        assertThat(actual.getUpdateId()).isNotNull();
        assertThat(actual.getVersion()).isEqualTo(2);
    }

    private void assertRoleOperation(RoleOperation actual, RoleId roleId, OperationId operataionId) {
        assertThat(actual.getRoleId()).isEqualTo(roleId);
        assertThat(actual.getOperationId()).isEqualTo(operataionId);
        assertThat(actual.getCreateDt()).isNotNull();
        assertThat(actual.getCreateId()).isNotNull();
    }

}
