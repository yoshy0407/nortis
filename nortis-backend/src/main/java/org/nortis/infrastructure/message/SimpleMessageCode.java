package org.nortis.infrastructure.message;

import lombok.AllArgsConstructor;

/**
 * シンプルな{@link MessageCode}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class SimpleMessageCode implements MessageCode {

	private final String messageId;
	
	private final String defaultMessageFormat;
	
	private final Object[] args;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCode() {
		return this.messageId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getArgs() {
		return this.args;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultMessage() {
		return new StringBuilder()
				.append("[%s]".formatted(this.messageId))
				.append(String.format(defaultMessageFormat, args))
				.toString();
	}

}
