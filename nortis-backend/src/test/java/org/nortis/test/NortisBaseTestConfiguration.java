package org.nortis.test;

import jakarta.annotation.PostConstruct;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.MessageSourceAccessor;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * テスト用のコンフィグクラス
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@SuppressWarnings("deprecation")
@Import(MessageSourceAutoConfiguration.class)
@TestConfiguration
public class NortisBaseTestConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	/**
	 * 初期化します
	 */
	@PostConstruct
	public void init() {
		ApplicationContextAccessor.set(null);
		ApplicationContextAccessor.set(applicationContext);
		MessageSourceAccessor.set(null);
		MessageSourceAccessor.set(applicationContext);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
}
