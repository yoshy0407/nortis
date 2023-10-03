package org.nortis.application.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DataNotFoundException;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.test.user.TestUsers;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
        "/META-INF/ddl/dropTenant.sql",
        "/ddl/createTenant.sql",
        "/META-INF/data/application/del_ins_tenant.sql"
})
//@formatter:on
class TenantQueryApplicationServiceTest extends RepositoryTestBase {

    @Mock
    AuthorityCheckDomainService authorityCheckDomainService;

    @Autowired
    Entityql entityql;

    TenantQueryApplicationService tenantQueryApplicationService;

    @BeforeEach
    void setup() {
        this.tenantQueryApplicationService = new TenantQueryApplicationService(entityql, authorityCheckDomainService);
    }

    @Test
    void testGet_success() throws DomainException {

        Tenant result = this.tenantQueryApplicationService.getTenant("0000000001",
                TestUsers.memberUser().getUserDetails(), ApplicationTranslator.noConvert());

        assertThat(result.getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(result.getTenantIdentifier()).isEqualTo(TenantIdentifier.create("TEST1"));
        assertThat(result.getTenantName()).isEqualTo("テストテナント1");
    }

    @Test
    void testGetTenant_NotFound() throws DomainException {
        assertThrows(DataNotFoundException.class, () -> {
            this.tenantQueryApplicationService.getTenant("0000000100", TestUsers.adminUser().getUserDetails(),
                    ApplicationTranslator.nothing());
        });
    }

    @Test
    void testGetListTenant() throws DomainException {
        Paging paging = new Paging(1, 5);
        List<Tenant> list = this.tenantQueryApplicationService.getListTenant(paging,
                TestUsers.memberUser().getUserDetails(), ApplicationTranslator.noConvert());

        assertThat(list).hasSize(5);
        assertThat(list.get(0).getTenantId()).isEqualTo(TenantId.create("0000000001"));
        assertThat(list.get(1).getTenantId()).isEqualTo(TenantId.create("0000000002"));
        assertThat(list.get(2).getTenantId()).isEqualTo(TenantId.create("0000000003"));
        assertThat(list.get(3).getTenantId()).isEqualTo(TenantId.create("0000000004"));
        assertThat(list.get(4).getTenantId()).isEqualTo(TenantId.create("0000000005"));
    }

}
