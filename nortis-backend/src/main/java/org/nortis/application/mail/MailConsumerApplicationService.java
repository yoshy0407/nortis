package org.nortis.application.mail;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.MailConsumer;
import org.nortis.domain.mail.MailConsumerDomainService;
import org.nortis.domain.mail.MailConsumerRepository;
import org.nortis.domain.mail.value.ConsumerId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

/**
 * メールコンシューマのアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class MailConsumerApplicationService {

	/** メールコンシューマリポジトリ */
	private final MailConsumerRepository mailConsumerRepository;
	
	/** メールコンシューマドメインサービス */
	private final MailConsumerDomainService mailConsumerDomainService;
	
	/**
	 * メールコンシューマを登録します
	 * @param <R> 結果クラス
	 * @param command メールコンシューマの登録コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 */
	public <R> R register(
			MailRegisterCommand command,
			ApplicationTranslator<MailConsumer, R> translator) {
		
		TenantId tenantId = TenantId.create(command.tenantId());
		EndpointId endpointId = EndpointId.create(command.endpointId());
		MailAddress mailAddress = MailAddress.create(command.mailAddress());
		
		MailConsumer mailConsumer = this.mailConsumerDomainService.createMailConsumer(
				tenantId, endpointId, mailAddress, command.userId());
		
		this.mailConsumerRepository.save(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	/**
	 * メールアドレスを変更します
	 * @param <R> 結果クラス
	 * @param command メールアドレス変更コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 */
	public <R> R changeMailAddress(
			MailAddressChangeCommand command,
			ApplicationTranslator<MailConsumer, R> translator) {
		ConsumerId consumerId = ConsumerId.create(command.consumerId());
		
		Optional<MailConsumer> optMailConsumer = 
				this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException("");
		}
		
		MailConsumer mailConsumer = optMailConsumer.get();
		
		MailAddress mailAddress = MailAddress.create(command.mailAddress());
		mailConsumer.updateMailAddress(mailAddress, command.userId());
		
		this.mailConsumerRepository.update(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	/**
	 * 削除します
	 * @param rawConsumerId コンシューマID
	 */
	public void delete(String rawConsumerId) {
		ConsumerId consumerId = ConsumerId.create(rawConsumerId);
		
		Optional<MailConsumer> optMailConsumer = 
				this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException("MSG30002");
		}
		this.mailConsumerRepository.remove(optMailConsumer.get());
	}
	
}
