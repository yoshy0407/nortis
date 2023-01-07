package org.nortis.infrastructure.template;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.velocity.app.VelocityEngine;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VelocityTemplateRenderTest {

	VelocityTemplateRender templateRender;
	
	@BeforeEach
	void setup() {
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		this.templateRender = new VelocityTemplateRender(ve);
	}
	
	@Test
	void testRender() {
		String template = "Hello world! ${params}!!";
		String output = this.templateRender.render("test", template, Maps.newHashMap("params", "John"));
		
		assertThat(output).isEqualTo("Hello world! John!!");
	}

	@Test
	void testRenderLn() {
		String template = """
				Hello ${name}
				This is sample message
				bye ${name}
				""";
		String output = this.templateRender.render("test", template, Maps.newHashMap("name", "John"));
		
		String expect = """
				Hello John
				This is sample message
				bye John			
				""";
		assertThat(output).isEqualTo(expect);
	}

}
