package org.nortis.infrastructure;

import org.nortis.infrastructure.template.TemplateRender;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

/**
 * {@link ApplicationContext}のアクセッサークラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ApplicationContextAccessor {

	/**
	 * {@link MessageSource}
	 */
	private static ApplicationContext applicationContext;
	
	/**
	 * コンストラクター
	 */
	private ApplicationContextAccessor() {
		throw new IllegalStateException("インスタンス化できません");
	}
	
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

}
