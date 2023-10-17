package org.nortis.test;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import java.util.List;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

/**
 * Logbackをモックするクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class LogMockit {

	private final Logger logger;
	
	private final Appender<ILoggingEvent> mockAppender;
	
	private final ArgumentCaptor<LoggingEvent> argumentCaptor;
	
	/**
	 * インスタンスを生成します
	 * @param logClass ログの対象クラス
	 */
	@SuppressWarnings("unchecked")
	public LogMockit(Class<?> logClass) {
		this.mockAppender = Mockito.mock(Appender.class);
		this.logger = (Logger) LoggerFactory.getLogger(logClass);
		this.argumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);
		
		this.logger.addAppender(mockAppender);
	}
	
	/**
	 * ロガーを取得します
	 * @return ロガー
	 */
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * モックの{@link Appender}を返却します
	 * @return モックの{@link Appender}
	 */
	public Appender<ILoggingEvent> getMockAppender() {
		return this.mockAppender;
	}
	
	/**
	 * ログ出力の検証を行います
	 * @return 出力されたログ情報のリスト
	 */
	public List<LoggingEvent> verify() {
		Mockito.verify(this.mockAppender).doAppend(this.argumentCaptor.capture());
		return this.argumentCaptor.getAllValues();
	}
	
}
