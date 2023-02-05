package org.nortis.domain.consumer.mail;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * メールコンシューマのドメインサービス
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class MailConsumerDomainService {

	/** エンドポイントリポジトリ */
	private final EndpointRepository endpointRepository;
	
	/**
	 * メールコンシューマを作成します
	 * @param tenantId テナントID
	 * @param endpointId エンドポイントID
	 * @param addressList メールアドレスのリスト
	 * @param userId ユーザID
	 * @return メールコンシューマ
	 * @throws DomainException ドメインロジックエラー
	 */
	public MailConsumer createMailConsumer(TenantId tenantId, EndpointId endpointId, 
			List<MailAddress> addressList, String userId) throws DomainException {		
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		
		if (optEndpoint.isEmpty()) {
			throw new DomainException(MessageCodes.nortis20001());
		}
		
		return MailConsumer.create(endpointId, addressList, userId);
	}
}
