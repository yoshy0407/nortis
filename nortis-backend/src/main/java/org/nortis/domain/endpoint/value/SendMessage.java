package org.nortis.domain.endpoint.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 送信するメッセージを表すオブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public class SendMessage {

	private final String subject;
	
	private final String message;
	
}
