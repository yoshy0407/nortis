package org.nortis.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * テナントに関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class TenantDomainService {

    private final TenantRepository tenantRepository;

    private final NumberingDomainService numberingDomainService;

    /**
     * 登録前の確認処理
     * 
     * @param tenantId         テナントID
     * @param tenantIdentifier テナント識別子
     * @throws DomainException 同じテナントが存在した場合
     */
    public void beforeRegisterCheck(TenantId tenantId, TenantIdentifier tenantIdentifier) throws DomainException {
        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isPresent()) {
            throw new UnexpectedException(MessageCodes.nortis10004(), null);
        }
        Optional<Tenant> optTenant2 = this.tenantRepository.getByTenantIdentifier(tenantIdentifier);
        if (optTenant2.isPresent()) {
            throw new DomainException(MessageCodes.nortis10001());
        }

    }

    /**
     * テナントを作成します
     * 
     * @param tenantId         テナントID
     * @param tenantIdentifier テナント識別子
     * @param tenantName       テナント名
     * @return テナント
     * @throws DomainException チェックエラー
     */
    public Tenant createTenant(TenantId tenantId, TenantIdentifier tenantIdentifier, String tenantName)
            throws DomainException {
        Tenant tenant = Tenant.create(tenantId, tenantIdentifier, tenantName);

        RoleId managementRoleId = this.numberingDomainService.createNewRoleId();
        List<OperationAuthority> managementAuthority = new ArrayList<>();
        managementAuthority.add(OperationAuthority.TENANT_MANAGEMENT_ROLE);
        tenant.createRole(managementRoleId, "管理者ロール", managementAuthority);

        RoleId readRoleId = this.numberingDomainService.createNewRoleId();
        List<OperationAuthority> readAuthority = new ArrayList<>();
        managementAuthority.add(OperationAuthority.TENANT_READ_ROLE);
        tenant.createRole(readRoleId, "読み取りロール", readAuthority);

        return tenant;
    }

    /**
     * ロールが存在するか確認します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @throws DomainException チェックエラー
     */
    public void checkExistRole(TenantId tenantId, RoleId roleId) throws DomainException {
        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("テナント"));
        }

        Tenant tenant = optTenant.get();

        if (!tenant.getRoles().containsKey(roleId)) {
            throw new DomainException(MessageCodes.nortis00003("テナントロール"));
        }
    }

    /**
     * テナントが存在するか確認します
     * 
     * @param tenantId テナントID
     * @throws DomainException チェックエラー
     */
    public void checkExistTenantOf(TenantId tenantId) throws DomainException {
        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("テナント"));
        }
    }
}
