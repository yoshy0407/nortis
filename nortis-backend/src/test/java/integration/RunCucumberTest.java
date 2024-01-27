package integration;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Tag("integration")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("integration")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
class RunCucumberTest {

}
