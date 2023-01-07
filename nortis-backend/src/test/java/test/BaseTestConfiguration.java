package test;

import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * テスト用のコンフィグクラス
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@TestConfiguration
public class BaseTestConfiguration implements ApplicationContextAware {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextAccessor.set(applicationContext);
		MessageSourceAccessor.set(applicationContext);		
	}

	
}
