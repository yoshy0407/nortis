package org.nortis.infrastructure.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * アプリケーションサービスを表す注釈型です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = Throwable.class)
@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ApplicationService {

	/**
	 * Bean名
	 * @return Bean名
	 */
	@AliasFor(attribute = "value", annotation = Service.class)
	String value() default "";
	
	/**
	 * スコープを指定します
	 * @return スコープ
	 */
	@AliasFor(attribute = "scopeName", annotation = Scope.class)
	String scope() default BeanDefinition.SCOPE_SINGLETON;

}
