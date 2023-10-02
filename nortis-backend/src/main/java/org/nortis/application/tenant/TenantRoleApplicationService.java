package org.nortis.application.tenant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.nortis.application.tenant.model.RoleAuthorityUpdateCommand;
import org.nortis.application.tenant.model.RoleCreateCommand;
import org.nortis.application.tenant.model.RoleNameChangeCommand;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * テナントのロールに関するサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Transactional
@AllArgsConstructor
@ApplicationService
public class TenantRoleApplicationService {

    private final TenantRepository tenantRepository;

    private final NumberingDomainService numberingDomainService;

    private final AuthorityCheckDomainService authorityCheckDomainService;

    /**
     * テナントの操作権限のリストを返却します
     * 
     * @param <R>        変換後のクラス
     * @param translator 変換処理
     * @return データのリスト
     */
    public <R> List<R> getOperationAuthorityList(ApplicationTranslator<OperationAuthority, R> translator) {
        // :NOTE 権限チェックが難しい
        //@formatter:off
        return Stream.of(OperationAuthority.values())
                .map(d -> translator.translate(d))
                .toList();
        //@formatter:on
    }

    /**
     * ロールを一件取得します
     * 
     * @param <R>         変換後のクラス
     * @param rawTenantId テナントID
     * @param rawRoleId   ロールID
     * @param nortisUser  認証ユーザ
     * @param translator  変換処理
     * @return 変換したロール
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R getRole(String rawTenantId, String rawRoleId, NortisUserDetails nortisUser,
            ApplicationTranslator<TenantRole, R> translator) throws DomainException {
        var tenantId = TenantId.create(rawTenantId);
        var roleId = RoleId.create(rawRoleId);

        Tenant tenant = getTenant(tenantId);

        this.authorityCheckDomainService.checkHasReadRole(nortisUser, tenant);

        Optional<TenantRole> tenantRole = tenant.getRole(roleId);
        if (tenantRole.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10005());
        }
        return translator.translate(tenantRole.get());
    }

    /**
     * テナントで保持しているロールを全て返却します
     * 
     * @param <R>         変換後のクラス
     * @param rawTenantId テナントID
     * @param nortisUser  認証ユーザ
     * @param translator  変換処理
     * @return 変換したリスト
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> List<R> getRoles(String rawTenantId, NortisUserDetails nortisUser,
            ApplicationTranslator<TenantRole, R> translator) throws DomainException {

        var tenantId = TenantId.create(rawTenantId);

        Tenant tenant = getTenant(tenantId);
        this.authorityCheckDomainService.checkHasReadRole(nortisUser, tenant);

        //@formatter:off
        return tenant.getRoles().entrySet().stream()
                .map(entry -> translator.translate(entry.getValue()))
                .toList();
        //@formatter:on
    }

    /**
     * ロールを作成します
     * 
     * @param <R>        変換後のクラス
     * @param command    作成コマンド
     * @param nortisUser 認証ユーザ
     * @param translator 変換処理
     * @return 変換後のクラス
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R createRole(RoleCreateCommand command, NortisUserDetails nortisUser,
            ApplicationTranslator<TenantRole, R> translator) throws DomainException {

        var tenantId = TenantId.create(command.tenantId());

        Tenant tenant = getTenant(tenantId);
        this.authorityCheckDomainService.checkHasWriteRole(nortisUser, tenant);

        RoleId roleId = this.numberingDomainService.createNewRoleId();
        List<OperationAuthority> authorityList = convertAuthority(command.authorityIds());

        tenant.createRole(roleId, command.roleName(), authorityList);

        this.tenantRepository.update(tenant);

        TenantRole tenantRole = tenant.getRoles().get(roleId);
        return translator.translate(tenantRole);
    }

    /**
     * ロール名を変更します
     * 
     * @param <R>        変換後のクラス
     * @param command    名前変更コマンド
     * @param nortisUser 認証ユーザ
     * @param translator 変換処理
     * @return 変換後のクラス
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R changeRoleName(RoleNameChangeCommand command, NortisUserDetails nortisUser,
            ApplicationTranslator<TenantRole, R> translator) throws DomainException {

        var tenantId = TenantId.create(command.tenantId());

        Tenant tenant = getTenant(tenantId);
        this.authorityCheckDomainService.checkHasWriteRole(nortisUser, tenant);

        RoleId roleId = RoleId.create(command.roleId());

        tenant.changeRoleNameOf(roleId, command.roleName());

        this.tenantRepository.update(tenant);

        TenantRole tenantRole = tenant.getRoles().get(roleId);
        return translator.translate(tenantRole);
    }

    /**
     * ロールの権限を更新します
     * 
     * @param <R>        戻り値のクラス
     * @param command    コマンド
     * @param nortisUser 認証ユーザ
     * @param translator 変換処理
     * @return 変換した値
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R updateOperationAuthority(RoleAuthorityUpdateCommand command, NortisUserDetails nortisUser,
            ApplicationTranslator<TenantRole, R> translator) throws DomainException {

        var tenantId = TenantId.create(command.tenantId());

        Tenant tenant = getTenant(tenantId);
        this.authorityCheckDomainService.checkHasWriteRole(nortisUser, tenant);

        RoleId roleId = RoleId.create(command.roleId());

        command.grantAuthorityList().forEach(authorityId -> {
            tenant.grantRoleAuthority(roleId, OperationAuthority.resolveAuthorityId(authorityId));
        });

        command.revokeAuthorityList().forEach(authorityId -> {
            tenant.revokeRoleAuthority(roleId, OperationAuthority.resolveAuthorityId(authorityId));
        });

        this.tenantRepository.update(tenant);

        return translator.translate(tenant.getRoles().get(roleId));
    }

    /**
     * ロールを削除します
     * 
     * @param rawTenantId テナントID
     * @param nortisUser  認証ユーザ
     * @param rawRoleId   ロールID
     * @throws DomainException ビジネスロジックエラー
     */
    public void deleteRole(String rawTenantId, NortisUserDetails nortisUser, String rawRoleId) throws DomainException {

        var tenantId = TenantId.create(rawTenantId);

        Tenant tenant = getTenant(tenantId);
        this.authorityCheckDomainService.checkHasWriteRole(nortisUser, tenant);

        RoleId roleId = RoleId.create(rawRoleId);

        tenant.deleteRole(roleId);

        this.tenantRepository.update(tenant);
    }

    private List<OperationAuthority> convertAuthority(List<String> authorityList) {
        //@formatter:off
        return authorityList.stream()
                .map(s -> OperationAuthority.resolveAuthorityId(s))
                .toList();
        //@formatter:on
    }

    private Tenant getTenant(TenantId tenantId) throws DomainException {
        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }
        return optTenant.get();

    }
}
