package org.nortis.domain.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.RepositoryTestBase;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.jdbc.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

//@formatter:off
@Sql(scripts = {
        "/META-INF/ddl/dropAuthentication.sql",
        "/ddl/createAuthentication.sql",
        "/META-INF/data/domain/del_ins_authentication.sql"
})
//@formatter:on
class AuthenticationRepositoryTest extends RepositoryTestBase {

    @Autowired
    Config config;

    AuthenticationRepository repository;

    @BeforeEach
    void setup() {
        this.repository = new AuthenticationRepositoryImpl(config);
    }

    @Test
    void testGet() throws DomainException {
        Optional<Authentication> optAuth = this.repository.get(ApiKey.create("APIKEYTENANT1"));

        assertThat(optAuth).isPresent();
        Authentication authentication = optAuth.get();
        assertThat(authentication.getApiKey()).isEqualTo(ApiKey.create("APIKEYTENANT1"));
        assertThat(authentication.getTenantId()).isEqualTo(TenantId.create("1000000001"));
        assertThat(authentication.getUserId()).isNull();
        assertThat(authentication.getLastAccessDatetime()).isEqualTo(LocalDateTime.of(2022, 1, 5, 12, 56, 34));
        assertThat(authentication.getCreateId()).isEqualTo("TEST");
        assertThat(authentication.getCreateDt()).isNotNull();
        assertThat(authentication.getUpdateId()).isNull();
        assertThat(authentication.getUpdateDt()).isNull();
        assertThat(authentication.getVersion()).isEqualTo(1);
    }

    @Test
    void testGetFromTenantId() throws DomainException {
        Optional<Authentication> optAuth = this.repository.getFromTenantId(TenantId.create("1000000001"));

        assertThat(optAuth).isPresent();
        Authentication authentication = optAuth.get();
        assertThat(authentication.getApiKey()).isEqualTo(ApiKey.create("APIKEYTENANT1"));
        assertThat(authentication.getTenantId()).isEqualTo(TenantId.create("1000000001"));
        assertThat(authentication.getUserId()).isNull();
        assertThat(authentication.getLastAccessDatetime()).isEqualTo(LocalDateTime.of(2022, 1, 5, 12, 56, 34));
        assertThat(authentication.getCreateId()).isEqualTo("TEST");
        assertThat(authentication.getCreateDt()).isNotNull();
        assertThat(authentication.getUpdateId()).isNull();
        assertThat(authentication.getUpdateDt()).isNull();
        assertThat(authentication.getVersion()).isEqualTo(1);
    }

    @Test
    void testGetFromUserId() throws DomainException {
        Optional<Authentication> optAuth = this.repository.getFromUserId(UserId.create("0000000001"));

        assertThat(optAuth).isPresent();
        Authentication authentication = optAuth.get();
        assertThat(authentication.getApiKey()).isEqualTo(ApiKey.create("APIKEYUSER1"));
        assertThat(authentication.getTenantId()).isNull();
        assertThat(authentication.getUserId()).isEqualTo(UserId.create("0000000001"));
        assertThat(authentication.getLastAccessDatetime()).isEqualTo(LocalDateTime.of(2022, 1, 5, 12, 0, 0));
        assertThat(authentication.getCreateId()).isEqualTo("TEST");
        assertThat(authentication.getCreateDt()).isNotNull();
        assertThat(authentication.getUpdateId()).isNull();
        assertThat(authentication.getUpdateDt()).isNull();
        assertThat(authentication.getVersion()).isEqualTo(1);
    }

    @Test
    void testGetUserAuthentication() throws DomainException {
        List<Authentication> resultList = this.repository.getUserAuthentication();

        assertThat(resultList).hasSize(3);
        assertThat(resultList.get(0).getApiKey()).isEqualTo(ApiKey.create("APIKEYUSER1"));
        assertThat(resultList.get(1).getApiKey()).isEqualTo(ApiKey.create("APIKEYUSER2"));
        assertThat(resultList.get(2).getApiKey()).isEqualTo(ApiKey.create("APIKEYUSER3"));

    }

    @Test
    void testSave() throws DomainException {

        var tenantId = TenantId.create("1000000099");

        Authentication authentication = Authentication.createFromTenant(tenantId);

        this.repository.save(authentication);

        Optional<Authentication> optAuth = this.repository.getFromTenantId(tenantId);
        assertThat(optAuth).isPresent();
    }

    @Test
    void testUpdate() throws DomainException {

        var userId = UserId.create("0000000001");

        Optional<Authentication> optAuth = this.repository.getFromUserId(userId);
        Authentication authentication = optAuth.get();

        LocalDateTime lastAccessDate = LocalDateTime.of(2023, 1, 4, 12, 34);
        authentication.setLastAccessDatetime(lastAccessDate);

        this.repository.update(authentication);

        Optional<Authentication> optAuth2 = this.repository.getFromUserId(userId);
        Authentication authentication2 = optAuth2.get();

        assertThat(authentication2.getLastAccessDatetime()).isEqualTo(lastAccessDate);
    }

    @Test
    void testRemove() throws DomainException {
        var userId = UserId.create("0000000001");

        Optional<Authentication> optAuth = this.repository.getFromUserId(userId);
        Authentication authentication = optAuth.get();

        this.repository.remove(authentication);

        Optional<Authentication> optAuth2 = this.repository.getFromUserId(userId);
        assertThat(optAuth2).isEmpty();
    }

}
