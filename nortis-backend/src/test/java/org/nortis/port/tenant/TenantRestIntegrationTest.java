package org.nortis.port.tenant;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nortis.NortisBackendApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {
		"/META-INF/ddl/dropAuthentication.sql",
		"/META-INF/ddl/dropSuser.sql",
		"/META-INF/ddl/dropTenant.sql",
		"/ddl/createAuthentication.sql",
		"/ddl/createSuser.sql",
		"/ddl/createTenant.sql",
		"/META-INF/data/port/del_ins_authentication.sql",
		"/META-INF/data/port/del_ins_suser.sql",
		"/META-INF/data/port/del_ins_tenant.sql"
})
@RecordApplicationEvents
@AutoConfigureDataJdbc
@SpringBootTest(classes = NortisBackendApplication.class)
class TenantRestIntegrationTest {

	@Autowired
	WebApplicationContext webApplicationContext;
	
	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}

	@Test
	void testGet() throws Exception {
		mockMvc.perform(get("/tenant/TEST1")
				.header("X-NORTIS-APIKEY", "APIKEYUSER1"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tenantId").value("TEST1"))
			.andExpect(jsonPath("$.tenantName").value("テストテナント１"));
	}

	@Test
	void testGetNotFound() throws Exception {
		mockMvc.perform(get("/tenant/YYYY")
				.header("X-NORTIS-APIKEY", "APIKEYUSER1"))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.code").value("NORTIS10003"))
			.andExpect(jsonPath("$.message").value("指定されたIDのテナントは存在しません"));
	}

	@Test
	void testRegister() throws Exception {
		mockMvc.perform(post("/tenant")
				.header("X-NORTIS-APIKEY", "APIKEYUSER3")
				.content("""
						{
							"tenantId": "MOCK",
							"tenantName": "テストテナント"
						}""")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tenantId").value("MOCK"))
			.andExpect(jsonPath("$.tenantName").value("テストテナント"));
	}

	@Test
	void testCreateApiKey() throws Exception {
		mockMvc.perform(post("/tenant/TEST1/apiKey")
				.header("X-NORTIS-APIKEY", "APIKEYUSER3"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.apiKey").exists());	
	}

	@Test
	void testUpdateName() throws Exception {
		mockMvc.perform(patch("/tenant/TEST2")
				.header("X-NORTIS-APIKEY", "APIKEYUSER2")
				.content("""
						{
							"name": "モックテナント"
						}""")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tenantId").value("TEST2"))
			.andExpect(jsonPath("$.tenantName").value("モックテナント"));	
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete("/tenant/TEST3")
				.header("X-NORTIS-APIKEY", "APIKEYUSER3"))
			.andDo(print())
			.andExpect(status().isNoContent());
		
	}

}
