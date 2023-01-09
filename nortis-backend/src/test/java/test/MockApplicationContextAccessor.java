package test;

import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.springframework.context.ApplicationContext;

/**
 * {@link ApplicationContextAccessor}のモックサポートクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class MockApplicationContextAccessor {

	private final ApplicationContext mock;
	
	/**
	 * インスタンスを生成します
	 */
	public MockApplicationContextAccessor() {
		this.mock = Mockito.mock(ApplicationContext.class);
		ApplicationContextAccessor.set(mock);
	}
	
	/**
	 * {@link ApplicationContext#getBean(Class)}をモックします
	 * @param <B> Beanクラス
	 * @param clazz クラス
	 * @param bean Beanインスタンス
	 */
	public <B> void mockGetBean(Class<B> clazz, B bean) {
		Mockito.when(mock.getBean(eq(clazz)))
			.thenReturn(bean);	
	}
	
	/**
	 * {@link ApplicationContextAccessor#getObjectMapper()}をモックします
	 * @param objectMapper {@link ObjectMapper}
	 */
	public void mockGetObjectMapper(ObjectMapper objectMapper) {
		mockGetBean(ObjectMapper.class, objectMapper);
	}
}
