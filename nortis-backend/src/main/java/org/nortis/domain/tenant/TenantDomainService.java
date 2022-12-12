package org.nortis.domain.tenant;

import java.util.List;

import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.springframework.data.domain.Example;

import lombok.AllArgsConstructor;

/**
 * テナントに関するドメインサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@DomainService
@AllArgsConstructor
public class TenantDomainService {

	/**
	 * テナントリポジトリ
	 */
	private final TenantRepository tenantRepository;

	/**
	 * 作成するテナントを返します
	 * @param tenantId テナント省略名
	 * @param tenantName テナント名
	 * @param createId 作成者ID
	 * @return テナント
	 */
	public Tenant createTenant(TenantId tenantId, String tenantName, String createId) {
		
		Example<Tenant> example = tenantShortNameCondition(tenantId);
		
		List<Tenant> tenantList = this.tenantRepository.findAll(example);
		
		if (!tenantList.isEmpty()) {
			throw new DomainException("MSG10001");
		}
		
		return Tenant.create(tenantId, tenantName, createId);
	}
	
	/**
	 * テナントIDを変更します
	 * @param currentTenantId 現在のテナント省略名
	 * @param changeTenantId 新しいテナント省略名
	 * @param updateId 更新者ID
	 * @return テナント
	 */
	public Tenant changeTenantId(TenantId currentTenantId, TenantId changeTenantId, String updateId) {
		
		List<Tenant> checkTenantList = this.tenantRepository.findByTenantId(changeTenantId);
		
		if (!checkTenantList.isEmpty()) {
			throw new DomainException("MSG10001");
		}
		
		List<Tenant> list = this.tenantRepository.findByTenantId(currentTenantId);
		
		if (list.size() != 1) {
			StringBuilder sb = new StringBuilder();
			for (Tenant entity : list) {
				sb.append(entity.toString());
				sb.append(System.getProperty("line.separator"));
			}
			throw new UnexpectedException("MSG10002", currentTenantId.toString(), sb.toString());
		}

		Tenant targetTenant = list.get(0);
		targetTenant.changeTenantId(changeTenantId, updateId);
		
		return targetTenant;
	}
	
	private Example<Tenant> tenantShortNameCondition(TenantId tenantId) {
		Tenant where = new Tenant();
		where.setTenantId(tenantId);
		return Example.of(where);
	}
}
