package org.nortis.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nortis.infrastructure.template.TemplateRender;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * {@link ApplicationContext}のアクセッサークラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Component
public class ApplicationContextAccessor implements ApplicationContextAware {

	/**
	 * {@link MessageSource}
	 */
	private static ApplicationContext applicationContext;
	
	/**
	 * {@link ApplicationContext}を設定します
	 * @param applicationContext {@link ApplicationContext}
	 */
	public static void set(ApplicationContext applicationContext) {
		ApplicationContextAccessor.applicationContext = applicationContext;
	}

	/**
	 * {@link TemplateRender}を返却します
	 * @return {@link TemplateRender}
	 */
	public static TemplateRender getTemplateRender() {
		return applicationContext.getBean(TemplateRender.class);
	}
	
	/**
	 * {@link ObjectMapper}を返却します
	 * @return {@link ObjectMapper}
	 */
	public static ObjectMapper getObjectMapper() {
		return applicationContext.getBean(ObjectMapper.class);
	}
	
	/**
	 * {@link ApplicationEventPublisher}を返却します
	 * @return {@link ApplicationEventPublisher}
	 */
	public static ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationContext;
	}
	
	/**
	 * {@link PasswordEncoder}を返却します
	 * @return {@link PasswordEncoder}
	 */
	public static PasswordEncoder getPasswordEncoder() {
		return applicationContext.getBean(PasswordEncoder.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextAccessor.set(applicationContext);
	}

}
