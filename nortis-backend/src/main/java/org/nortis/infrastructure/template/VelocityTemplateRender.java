package org.nortis.infrastructure.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.nortis.infrastructure.exception.UnexpectedException;

/**
 * Velocityで実装した{@link TemplateRender}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class VelocityTemplateRender implements TemplateRender {

	private final VelocityEngine velocityEngine;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String render(String endpointId, String templateText, Map<String, Object> parameter) {
		try (StringWriter writer = new StringWriter()) {
			this.velocityEngine.evaluate(new VelocityContext(parameter), writer, endpointId, templateText);
			return writer.toString();
		} catch (IOException ex) {
			throw new UnexpectedException("MSG90003", ex);
		}
	}

}
