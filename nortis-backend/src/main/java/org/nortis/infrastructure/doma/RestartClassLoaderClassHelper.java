package org.nortis.infrastructure.doma;

import org.seasar.doma.jdbc.ClassHelper;
import org.springframework.beans.factory.BeanClassLoaderAware;

/**
 * 特殊なクラスローダをDomaでつけるようにした{@link ClassHelper}です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class RestartClassLoaderClassHelper implements ClassHelper, BeanClassLoaderAware {

	/** 特殊なクラスローダー */
	private ClassLoader classLoader;
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Class<T> forName(String className) throws Exception {
		return (Class<T>) this.classLoader.loadClass(className);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

}
