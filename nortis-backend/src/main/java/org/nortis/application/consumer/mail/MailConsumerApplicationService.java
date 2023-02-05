package org.nortis.application.consumer.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.mail.MailConsumer;
import org.nortis.domain.consumer.mail.MailConsumerDomainService;
import org.nortis.domain.consumer.mail.MailConsumerRepository;
import org.nortis.domain.consumer.mail.value.ConsumerId;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

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
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R register(
			MailRegisterCommand command,
			ApplicationTranslator<MailConsumer, R> translator) throws DomainException {
		
		TenantId tenantId = TenantId.create(command.tenantId());
		EndpointId endpointId = EndpointId.create(command.endpointId());
		List<MailAddress> mailAddressList = new ArrayList<>();
		for (String mailAddress : command.mailAddressList()) {
			mailAddressList.add(MailAddress.create(mailAddress));
		}
		
		MailConsumer mailConsumer = this.mailConsumerDomainService.createMailConsumer(
				tenantId, endpointId, mailAddressList, command.userId());
		
		this.mailConsumerRepository.save(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	/**
	 * メールアドレスを変更します
	 * @param <R> 結果クラス
	 * @param command メールアドレス変更コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R addMailAddress(
			MailAddressAddCommand command,
			ApplicationTranslator<MailConsumer, R> translator) throws DomainException {
		ConsumerId consumerId = ConsumerId.create(command.consumerId());
		
		Optional<MailConsumer> optMailConsumer = 
				this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException(MessageCodes.nortis30005());
		}
		
		MailConsumer mailConsumer = optMailConsumer.get();
		
		for (String mailAddress : command.mailAddressList()) {
			mailConsumer.addMailAddress(MailAddress.create(mailAddress));
		}
		
		this.mailConsumerRepository.update(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	/**
	 * メールアドレスを削除します
	 * @param <R> 結果クラス
	 * @param command メールアドレス削除コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R deleteMailAddress(
			MailAddressDeleteCommand command,
			ApplicationTranslator<MailConsumer, R> translator) throws DomainException {
		ConsumerId consumerId = ConsumerId.create(command.consumerId());
		
		Optional<MailConsumer> optMailConsumer = 
				this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException(MessageCodes.nortis30005());
		}
		
		MailConsumer mailConsumer = optMailConsumer.get();
		
		for (String mailAddress : command.mailAddressList()) {
			mailConsumer.removeMailAddress(MailAddress.create(mailAddress));
		}
		
		this.mailConsumerRepository.update(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	/**
	 * 削除します
	 * @param rawConsumerId コンシューマID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void delete(String rawConsumerId) throws DomainException {
		ConsumerId consumerId = ConsumerId.create(rawConsumerId);
		
		Optional<MailConsumer> optMailConsumer = 
				this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException(MessageCodes.nortis30002());
		}
		this.mailConsumerRepository.remove(optMailConsumer.get());
	}
	
	/**
	 * エンドポイント削除による対象レコードのエンドポイント設定を削除します
	 * @param endpointId エンドポイントID
	 * @param userId ユーザID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void removeEndpointIdByEndpointDeleted(String endpointId, String userId) throws DomainException {
		EndpointId enId = EndpointId.create(endpointId);
		List<MailConsumer> mailConsumers = this.mailConsumerRepository.getFromEndpoint(enId);
		
		if (mailConsumers.isEmpty()) {
			return;
		}
		
		mailConsumers.forEach(data -> {
			data.resetEndpointId(userId);
			this.mailConsumerRepository.update(data);
		});
	}
}
