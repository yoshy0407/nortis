package org.nortis.infrastructure.doma.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.UserRole;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
    "/META-INF/ddl/dropSUser.sql",
    "/ddl/createSuser.sql",
    "/META-INF/data/domain/del_ins_suser.sql"
})
//@formatter:on
class DomaSuserRepositoryTest extends RepositoryTestBase {

    @Autowired
    Entityql entityql;

    SuserRepository repository;

    @BeforeEach
    void setup() {
        this.repository = new DomaSuserRepository(entityql);
    }

    @Test
    void testGetByUserId() throws DomainException {

        var userId = UserId.create("0000000001");
        Optional<Suser> optSuser = this.repository.getByUserId(userId);

        assertThat(optSuser).isPresent();

        Suser suser = optSuser.get();
        assertThat(suser.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(suser.getUsername()).isEqualTo("テストユーザ１");
        assertThat(suser.getLoginId()).isEqualTo(LoginId.create("USER1"));
        assertThat(suser.getHashedPassword()).isEqualTo(HashedPassword.create("password"));
        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
        assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.MEMBER);
        assertThat(suser.getCreateId()).isNotNull();
        assertThat(suser.getCreateDt()).isNotNull();
        assertThat(suser.getUpdateId()).isNull();
        assertThat(suser.getUpdateDt()).isNull();
        assertThat(suser.getVersion()).isEqualTo(1L);

        assertThat(suser.getUserRoles()).hasSize(2);
        UserRole tenantUser1 = suser.getUserRoles().get(0);
        assertThat(tenantUser1.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser1.getTenantId()).isEqualTo(TenantId.create("1000000001"));
        assertThat(tenantUser1.getRoleId()).isEqualTo(RoleId.create("00001"));
        UserRole tenantUser2 = suser.getUserRoles().get(1);
        assertThat(tenantUser2.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser2.getTenantId()).isEqualTo(TenantId.create("1000000002"));
        assertThat(tenantUser2.getRoleId()).isEqualTo(RoleId.create("00002"));
    }

    @Test
    void testGetByLoginId() throws DomainException {
        var loginId = LoginId.create("USER1");
        Optional<Suser> optSuser = this.repository.getByLoginId(loginId);

        assertThat(optSuser).isPresent();

        Suser suser = optSuser.get();
        assertThat(suser.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(suser.getUsername()).isEqualTo("テストユーザ１");
        assertThat(suser.getLoginId()).isEqualTo(LoginId.create("USER1"));
        assertThat(suser.getHashedPassword()).isEqualTo(HashedPassword.create("password"));
        assertThat(suser.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
        assertThat(suser.getAdminFlg()).isEqualTo(AdminFlg.MEMBER);
        assertThat(suser.getCreateId()).isNotNull();
        assertThat(suser.getCreateDt()).isNotNull();
        assertThat(suser.getUpdateId()).isNull();
        assertThat(suser.getUpdateDt()).isNull();
        assertThat(suser.getVersion()).isEqualTo(1L);

        assertThat(suser.getUserRoles()).hasSize(2);
        UserRole tenantUser1 = suser.getUserRoles().get(0);
        assertThat(tenantUser1.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser1.getTenantId()).isEqualTo(TenantId.create("1000000001"));
        assertThat(tenantUser1.getRoleId()).isEqualTo(RoleId.create("00001"));
        UserRole tenantUser2 = suser.getUserRoles().get(1);
        assertThat(tenantUser2.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser2.getTenantId()).isEqualTo(TenantId.create("1000000002"));
        assertThat(tenantUser2.getRoleId()).isEqualTo(RoleId.create("00002"));
    }

    @Test
    void testSave() throws DomainException {

        var userId = UserId.create("0000000100");
        var tenantId = TenantId.create("1000000001");
        var roleId = RoleId.create("00001");
        var loginId = LoginId.create("C_USER");
        var hashedPassword = HashedPassword.create("password");

        Suser suser = Suser.create(userId, "登録ユーザ", AdminFlg.ADMIN, Maps.newHashMap(tenantId, roleId), loginId,
                hashedPassword);

        this.repository.save(suser);

        Optional<Suser> optSuser = this.repository.getByLoginId(loginId);

        assertThat(optSuser).isPresent();

        Suser suser2 = optSuser.get();
        assertThat(suser2.getUserId()).isEqualTo(userId);
        assertThat(suser2.getUsername()).isEqualTo("登録ユーザ");
        assertThat(suser2.getLoginId()).isEqualTo(loginId);
        assertThat(suser2.getHashedPassword()).isEqualTo(hashedPassword);
        assertThat(suser2.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
        assertThat(suser2.getAdminFlg()).isEqualTo(AdminFlg.ADMIN);
        assertThat(suser2.getCreateId()).isNotNull();
        assertThat(suser2.getCreateDt()).isNotNull();
        assertThat(suser2.getUpdateId()).isNull();
        assertThat(suser2.getUpdateDt()).isNull();
        assertThat(suser2.getVersion()).isEqualTo(1L);

        assertThat(suser.getUserRoles()).hasSize(1);
        UserRole tenantUser1 = suser.getUserRoles().get(0);
        assertThat(tenantUser1.getUserId()).isEqualTo(userId);
        assertThat(tenantUser1.getTenantId()).isEqualTo(tenantId);
        assertThat(tenantUser1.getRoleId()).isEqualTo(roleId);
    }

    @Test
    void testUpdate() throws DomainException {

        var userId = UserId.create("0000000001");
        Optional<Suser> optSuser = this.repository.getByUserId(userId);

        Suser suser = optSuser.get();

        suser.changeUsername("サンプルユーザ");
        suser.grantTenantAccessOf(TenantId.create("1100000001"), RoleId.create("10001"));
        suser.revokeTenantAccessOf(TenantId.create("1000000001"), RoleId.create("00001"));

        this.repository.update(suser);

        Optional<Suser> optSuser2 = this.repository.getByUserId(userId);

        Suser suser2 = optSuser2.get();
        assertThat(suser2.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(suser2.getUsername()).isEqualTo("サンプルユーザ");
        assertThat(suser2.getLoginId()).isEqualTo(LoginId.create("USER1"));
        assertThat(suser2.getHashedPassword()).isEqualTo(HashedPassword.create("password"));
        assertThat(suser2.getLoginFlg()).isEqualTo(LoginFlg.NOT_LOGIN);
        assertThat(suser2.getAdminFlg()).isEqualTo(AdminFlg.MEMBER);
        assertThat(suser2.getCreateId()).isNotNull();
        assertThat(suser2.getCreateDt()).isNotNull();
        assertThat(suser2.getUpdateId()).isNotNull();
        assertThat(suser2.getUpdateDt()).isNotNull();
        assertThat(suser2.getVersion()).isEqualTo(2L);

        assertThat(suser2.getUserRoles()).hasSize(2);
        UserRole tenantUser1 = suser2.getUserRoles().get(0);
        assertThat(tenantUser1.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser1.getTenantId()).isEqualTo(TenantId.create("1000000002"));
        assertThat(tenantUser1.getRoleId()).isEqualTo(RoleId.create("00002"));
        UserRole tenantUser2 = suser2.getUserRoles().get(1);
        assertThat(tenantUser2.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(tenantUser2.getTenantId()).isEqualTo(TenantId.create("1100000001"));
        assertThat(tenantUser2.getRoleId()).isEqualTo(RoleId.create("10001"));
    }

    @Test
    void testRemove() throws DomainException {

        var userId = UserId.create("0000000002");
        Optional<Suser> optSuser = this.repository.getByUserId(userId);

        this.repository.remove(optSuser.get());

        Optional<Suser> optSuser2 = this.repository.getByUserId(userId);

        assertThat(optSuser2).isEmpty();

    }

}
