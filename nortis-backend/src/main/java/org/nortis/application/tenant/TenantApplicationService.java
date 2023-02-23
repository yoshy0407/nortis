package org.nortis.application.tenant;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.AuthorityCheckDomainService;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;

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
	
	/** 権限チェックのドメインサービス */
	private final AuthorityCheckDomainService authorityCheckDomainService;
	
	/**
	 * テナントを取得します
	 * @param <R> 結果クラス
	 * @param rawTenantId テナントIDの文字列
	 * @param user ユーザ
	 * @param translator トランスレータ
	 * @return テナント
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R getTenant(String rawTenantId, NortisUserDetails user, ApplicationTranslator<Tenant, R> translator) throws DomainException {
		TenantId tenantId = TenantId.create(rawTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}

		Tenant tenant = optTenant.get();

		//権限チェック
		authorityCheckDomainService.checkTenantAuthority(user, tenant);
		
		return translator.translate(tenant);
	}
	
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
		
		//権限のチェック
		this.authorityCheckDomainService.checkAdminOf(UserId.create(command.user().getUsername()));
		
		Tenant tenant = this.tenantDomainService.createTenant(
				tenantId, command.name(), command.user().getUsername());
		
		this.tenantRepository.save(tenant);
		
		return translator.translate(tenant);
	}
	
	/**
	 * テナントのAPIキーを作成します
	 * @param rawTenantId テナントID
	 * @param user ユーザ
	 * @return APIキー
	 * @throws DomainException ドメインロジックエラー
	 */
	public ApiKey createApiKey(String rawTenantId, NortisUserDetails user) throws DomainException {
		TenantId tenantId = TenantId.create(rawTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}
		
		Tenant tenant = optTenant.get();
		//権限チェック
		authorityCheckDomainService.checkTenantAuthority(user, tenant);
		
		return this.authenticationDomainService.createApiKeyOf(tenant);
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
		
		//権限チェック
		this.authorityCheckDomainService.checkTenantAuthority(command.user(), tenant);
		
		tenant.changeTenantName(command.name(), command.user().getUsername());
		
		this.tenantRepository.update(tenant);
		
		return translator.translate(tenant);
	}
	
	/**
	 * テナントを削除します
	 * @param rawTenantId テナントID
	 * @param user ユーザ
	 * @throws DomainException ドメインロジックエラー
	 */
	public void delete(String rawTenantId, NortisUserDetails user) throws DomainException {
		TenantId tenantId = TenantId.create(rawTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException(MessageCodes.nortis10003());
		}
		Tenant tenant = optTenant.get();
		
		this.authorityCheckDomainService.checkTenantAuthority(user, tenant);
		
		tenant.deleted(user.getUsername());
		this.tenantRepository.remove(tenant);
	}
}
