package org.nortis.application.endpoint;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * エンドポイントのアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class EndpointApplicationService {

	/** テナントリポジトリ */
	private final TenantRepository tenantRepository;
	
	/** エンドポイントリポジトリ */
	private final EndpointRepository endpointRepository;
	
	/**
	 * エンドポイントを登録します
	 * @param <R> 結果のクラス
	 * @param command エンドポイント登録のコマンド
	 * @param translator 結果を変換する処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R registerEndpoint(
			EndpointRegisterCommand command, 
			ApplicationTranslator<Endpoint, R> translator) throws DomainException {
		
		TenantId tenantId = TenantId.create(command.tenantId());
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}
		
		EndpointId endpointId = EndpointId.create(command.endpointId());
		
		Endpoint endpoint = tenant.get()
				.createEndpoint(
						endpointId, 
						command.endpointName(), 
						command.subjectTemplate(),
						command.messageTemplate(),
						command.userId());
		
		this.endpointRepository.save(endpoint);
		
		return translator.translate(endpoint);
	}
	
	/**
	 * エンドポイントを更新します
	 * @param <R> 結果クラス
	 * @param command エンドポイント更新コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R updateEndpoint(
			EndpointUpdateCommand command,
			ApplicationTranslator<Endpoint, R> translator) throws DomainException {
		TenantId tenantId = TenantId.create(command.tenantId());
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}

		EndpointId endpointId = EndpointId.create(command.endpointId());
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		if (optEndpoint.isEmpty()) {
			throw new DomainException(MessageCodes.nortis20001());
		}
		
		Endpoint endpoint = optEndpoint.get();
		endpoint.changeEndpointName(command.endpointName(), command.userId());
		endpoint.changeSubjectTemplate(command.subjectTemplate(), command.userId());
		endpoint.changeMessageTemplate(command.messageTemplate(), command.userId());
		this.endpointRepository.update(endpoint);
		
		return translator.translate(endpoint);
	}
	
	/**
	 * エンドポイントを削除します
	 * @param command 削除コマンド
	 * @throws DomainException ドメインロジックエラー
	 */
	public void delete(EndpointDeleteCommand command) throws DomainException {
		TenantId tenantId = TenantId.create(command.tenantId());
		EndpointId endpointId = EndpointId.create(command.endpointId());
		
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		if (optEndpoint.isEmpty()) {
			throw new DomainException(MessageCodes.nortis20001());
		}
		Endpoint endpoint = optEndpoint.get();
		endpoint.deleted(command.userId());
		this.endpointRepository.remove(endpoint);
	}
	
	/**
	 * 指定されたテナントのエンドポイントを削除します
	 * @param tenantId テナントID
	 * @param userId ユーザID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void deleteFromTenantId(String tenantId, String userId) throws DomainException {
		TenantId id = TenantId.create(tenantId);
		List<Endpoint> endpointList = this.endpointRepository.getFromTenantId(id);
		
		endpointList.forEach(endpoint -> endpoint.deleted(userId));
		
		this.endpointRepository.removeAll(endpointList);
	}
}
