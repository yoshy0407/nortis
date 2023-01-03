package org.nortis.application.endpoint;

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
	 */
	public <R> R registerEndpoint(
			EndpointRegisterCommand command, 
			ApplicationTranslator<Endpoint, R> translator) {
		
		TenantId tenantId = TenantId.create(command.tenantId());
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		
		EndpointId endpointId = EndpointId.create(command.endpointId());
		
		Endpoint endpoint = tenant.get()
				.createEndpoint(
						endpointId, 
						command.endpointName(), 
						command.userId());
		
		this.endpointRepository.save(endpoint);
		
		return translator.translate(endpoint);
	}
	
	/**
	 * エンドポイント名を変更します
	 * @param <R> 結果クラス
	 * @param command エンドポイント名更新コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 */
	public <R> R changeName(
			EndpointNameUpdateCommand command,
			ApplicationTranslator<Endpoint, R> translator) {
		TenantId tenantId = TenantId.create(command.tenantId());
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		
		if (tenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}

		EndpointId endpointId = EndpointId.create(command.endpointId());
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		if (optEndpoint.isEmpty()) {
			throw new DomainException("MSG20001");
		}
		
		Endpoint endpoint = optEndpoint.get();
		endpoint.changeEndpointName(command.endpointName(), command.userId());
		this.endpointRepository.update(endpoint);
		
		return translator.translate(endpoint);
	}
	
	/**
	 * エンドポイントを削除します
	 * @param command 削除コマンド
	 */
	public void delete(EndpointDeleteCommand command) {
		TenantId tenantId = TenantId.create(command.tenantId());
		EndpointId endpointId = EndpointId.create(command.endpointId());
		
		//:TODO Check Event
		
		Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
		if (optEndpoint.isEmpty()) {
			throw new DomainException("MSG20001");
		}
		
		this.endpointRepository.remove(optEndpoint.get());
	}
}
