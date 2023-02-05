package org.nortis.application.tenant;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * テナントのアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class TenantApplicationService {

	/** テナントリポジトリ */
	private final TenantRepository tenantRepository;
	
	/** テナントドメインサービス */
	private final TenantDomainService tenantDomainService;
	
	private final AuthenticationDomainService authenticationDomainService;
	
	/**
	 * テナントを登録します
	 * @param <R> 結果クラス
	 * @param command 登録コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R register(
			TenantRegisterCommand command,
			ApplicationTranslator<Tenant, R> translator) throws DomainException {
		
		TenantId tenantId = TenantId.create(command.tenantId());
		Tenant tenant = this.tenantDomainService.createTenant(
				tenantId, command.name(), command.userId());
		
		this.tenantRepository.save(tenant);
		
		return translator.translate(tenant);
	}
	
	/**
	 * テナントのAPIキーを作成します
	 * @param rawTenantId テナントID
	 * @return APIキー
	 * @throws DomainException ドメインロジックエラー
	 */
	public ApiKey createApiKey(String rawTenantId) throws DomainException {
		TenantId tenantId = TenantId.create(rawTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}
		
		return this.authenticationDomainService.createApiKeyOf(optTenant.get());
	}
	
	/**
	 * テナント名を変更します
	 * @param <R> 結果クラス
	 * @param command テナント名更新のコマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R changeName(
			TenantNameUpdateCommand command,
			ApplicationTranslator<Tenant, R> translator) throws DomainException {
		
		TenantId tenantId = TenantId.create(command.tenantId());

		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}
		
		Tenant tenant = optTenant.get();
		
		tenant.changeTenantName(command.name(), command.userId());
		
		this.tenantRepository.update(tenant);
		
		return translator.translate(tenant);
	}
	
	/**
	 * テナントを削除します
	 * @param rawTenantId テナントID
	 * @param userId ユーザID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void delete(String rawTenantId, String userId) throws DomainException {
		TenantId tenantId = TenantId.create(rawTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}
		Tenant tenant = optTenant.get();
		tenant.deleted(userId);
		this.tenantRepository.remove(tenant);
	}
}
