package org.nortis.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.seasar.doma.jdbc.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
        "/META-INF/ddl/dropTenant.sql",
        "/META-INF/ddl/dropSuser.sql",
        "/META-INF/ddl/dropEndpoint.sql",
        "/META-INF/ddl/dropConsumer.sql",
        "/ddl/createTenant.sql",
        "/ddl/createSuser.sql",
        "/ddl/createEndpoint.sql",
        "/ddl/createConsumer.sql"
})
//@formatter:on
class NumberingDomainServiceTest extends RepositoryTestBase {

    @Autowired
    Config config;

    NumberingDomainService domainService;

    @BeforeEach
    void setup() {
        this.domainService = new NumberingDomainService(config);
    }

    @Test
    void testCreateNewUserId() {
        var userId = this.domainService.createNewUserId();

        assertThat(userId).isEqualTo(UserId.createNew(1));
    }

    @Test
    void testCreateNewTenantId() {
        var tenantId = this.domainService.createNewTenantId();

        assertThat(tenantId).isEqualTo(TenantId.createNew(1));
    }

    @Test
    void testCreateNewRoleId() {
        var roleId = this.domainService.createNewRoleId();

        assertThat(roleId).isEqualTo(RoleId.createNew(1));
    }

    @Test
    void testCreateNewEndpointId() {
        var endpointId = this.domainService.createNewEndpointId();

        assertThat(endpointId).isEqualTo(EndpointId.createNew(1));
    }

    void testCreateNewConsumerId() {
        var consumerId = this.domainService.createNewConsumerId();
        assertThat(consumerId).isEqualTo(ConsumerId.createNew(1));
    }

}
