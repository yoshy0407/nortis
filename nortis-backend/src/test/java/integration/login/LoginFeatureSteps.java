package integration.login;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import java.util.List;
import org.nortis.application.authentication.UserLoginService;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@Sql(scripts = "/ddl/createSuser.sql")
@CucumberContextConfiguration
@AutoConfigureDataJdbc
@SpringBootTest(classes = { UserLoginTestConfig.class, DomaAutoConfiguration.class })
public class LoginFeatureSteps {

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    SuserRepository suserRepository;

    Authentication authentication;

    DomainException ex;

    @Given("ユーザテーブルには次のデータが入っている:")
    public void ユーザテーブルには次のデータが入っている(io.cucumber.datatable.DataTable dataTable) throws DomainException {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.

        List<String> row = dataTable.row(1);
        Suser suser = Suser.create(UserId.create(row.get(0)), row.get(1), AdminFlg.ADMIN, Collections.emptyMap(),
                LoginId.create(row.get(2)), HashedPassword.create(row.get(3)));

        suserRepository.save(suser);
    }

    @When("ユーザが、ユーザID: {string}、パスワード: {string} でログインした")
    public void ユーザは_ユーザid_パスワード_でログインした(String string, String string2) throws Exception {
        try {
            this.authentication = this.userLoginService.login(string, string2, ApplicationTranslator.noConvert());
        } catch (DomainException ex) {
            this.ex = ex;
        }
    }

    @Then("APIキーが発行される")
    public void apiキーが発行される() {
        assertThat(this.authentication.getApiKey()).isNotNull();
        assertThat(this.ex).isNull();
    }

    @Then("例外になる")
    public void 例外になる() {
        assertThat(this.authentication).isNull();
        assertThat(this.ex.getMessageId()).isEqualTo("NORTIS50004");
    }
}
