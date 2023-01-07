package org.nortis.domain.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;

/**
 * 送信メッセージを表すオブジェクトです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public class Message {

	/** テナントID */
	private final TenantId tenantId;
	
	/** エンドポイントID */
	private final EndpointId endpointId;
	
	/** 件名 */
	private final String subject;
	
	/** メッセージ本文 */
	private final String messageBody;

}
