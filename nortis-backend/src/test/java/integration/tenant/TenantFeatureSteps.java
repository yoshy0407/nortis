package integration.tenant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.util.Optional;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TenantFeatureSteps {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TenantRepository tenantRepository;

    MvcResult mvcResult;

    @Given("テナントテーブルにはデータは存在しない")
    public void テナントテーブルにはデータは存在しない() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("管理者が、テナント識別子: {string}、テナント名: {string} でテナントの作成を依頼した")
    public void 管理者が_テナント識別子_テナント名_でテナントの作成を依頼した(String string, String string2) throws Exception {

        String json = "";

        this.mvcResult = this.mockMvc.perform(post("/tenant").content("")).andReturn();
    }

    @Then("テナント識別子: {string} でテナントが作成される")
    public void テナント識別子_でテナントが作成される(String string) throws DomainException {
        Optional<Tenant> tenant = this.tenantRepository.getByTenantIdentifier(TenantIdentifier.create(string));
        assertThat(tenant).isNotEmpty();
    }
}
