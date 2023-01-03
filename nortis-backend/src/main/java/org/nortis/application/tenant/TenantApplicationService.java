package org.nortis.application.tenant;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

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
	
	/**
	 * テナントを登録します
	 * @param <R> 結果クラス
	 * @param command 登録コマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 */
	public <R> R register(
			TenantRegisterCommand command,
			ApplicationTranslator<Tenant, R> translator) {
		
		TenantId tenantId = TenantId.create(command.tenantId());
		Tenant tenant = this.tenantDomainService.createTenant(
				tenantId, command.name(), command.userId());
		
		this.tenantRepository.save(tenant);
		
		return translator.translate(tenant);
	}
	
	/**
	 * テナント名を変更します
	 * @param <R> 結果クラス
	 * @param command テナント名更新のコマンド
	 * @param translator 変換処理
	 * @return 処理結果
	 */
	public <R> R changeName(
			TenantNameUpdateCommand command,
			ApplicationTranslator<Tenant, R> translator) {
		
		TenantId tenantId = TenantId.create(command.tenantId());

		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		
		Tenant tenant = optTenant.get();
		
		tenant.changeTenantName(command.name(), command.userId());
		
		this.tenantRepository.update(tenant);
		
		return translator.translate(tenant);
	}
	
	/**
	 * テナントを削除します
	 * @param rawTenantId テナントID
	 */
	public void delete(String rawTenantId) {
		TenantId tenantId = TenantId.create(rawTenantId);
		
		Optional<Tenant> optTenant = this.tenantRepository.get(tenantId);
		if (optTenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		this.tenantRepository.remove(optTenant.get());
	}
}
