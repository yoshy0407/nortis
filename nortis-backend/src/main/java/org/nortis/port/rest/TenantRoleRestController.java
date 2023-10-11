package org.nortis.port.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.application.tenant.TenantRoleApplicationService;
import org.nortis.application.tenant.model.RoleAuthorityUpdateCommand;
import org.nortis.application.tenant.model.RoleCreateCommand;
import org.nortis.application.tenant.model.RoleNameChangeCommand;
import org.nortis.domain.tenant.TenantRole;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.port.rest.payload.TenantRoleRequest;
import org.nortis.port.rest.payload.TenantRoleUpdateAuthorityRequest;
import org.nortis.port.rest.payload.TenantRoleUpdateNameRequest;
import org.nortis.port.rest.resource.RoleOperationResource;
import org.nortis.port.rest.resource.TenantRoleResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * テナントロールのコントローラです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RequestMapping("/tenant/{tenantId}/role")
@RestController
public class TenantRoleRestController {

    private final TenantRoleApplicationService tenantRoleApplicationService;

    /**
     * テナントロールを取得します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @param user     認証ユーザ
     * @return テナントロール
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{roleId}")
    public TenantRoleResource get(@PathVariable String tenantId, @PathVariable String roleId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.tenantRoleApplicationService.getRole(tenantId, roleId, user, translator());
    }

    /**
     * テナントロールを複数件取得します
     * 
     * @param tenantId テナントID
     * @param user     認証ユーザ
     * @return 結果
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TenantRoleResource> getList(@PathVariable String tenantId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.tenantRoleApplicationService.getRoles(tenantId, user, translator());
    }

    /**
     * テナントロールの登録
     * 
     * @param tenantId テナントID
     * @param request  登録リクエスト
     * @param user     認証ユーザ
     * @return 登録結果
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public TenantRoleResource createRole(@PathVariable String tenantId,
            @Validated @RequestBody TenantRoleRequest request, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        //@formatter:off
        RoleCreateCommand command = RoleCreateCommand.builder()
                .tenantId(tenantId)
                .roleName(request.getRoleName())
                .authorityIds(request.getAuthorities())
                .build();
        //@formatter:on
        return this.tenantRoleApplicationService.createRole(command, user, translator());
    }

    /**
     * テナントのロール名を更新します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @param request  リクエストボディ
     * @param user     認証ユーザ
     * @return 変更結果
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{roleId}/updateName")
    public TenantRoleResource updateName(@PathVariable String tenantId, @PathVariable String roleId,
            @RequestBody @Validated TenantRoleUpdateNameRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        //@formatter:off
        RoleNameChangeCommand command = RoleNameChangeCommand.builder()
                .tenantId(tenantId)
                .roleId(roleId)
                .roleName(request.getRoleName())
                .build();
        //@formatter:on

        return this.tenantRoleApplicationService.changeRoleName(command, user, translator());
    }

    /**
     * テナントロールの権限を更新します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @param request  リクエストボディ
     * @param user     認証ユーザ
     * @return 結果
     * @throws DomainException ロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{roleId}/authority")
    public TenantRoleResource updateAuthority(@PathVariable String tenantId, @PathVariable String roleId,
            @RequestBody @Validated TenantRoleUpdateAuthorityRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        //@formatter:off
        RoleAuthorityUpdateCommand command = RoleAuthorityUpdateCommand.builder()
                .tenantId(tenantId)
                .roleId(roleId)
                .grantAuthorityList(request.getGrantAuthorities())
                .revokeAuthorityList(request.getRevokeAuthorities())
                .build();
        //@formatter:on
        return this.tenantRoleApplicationService.updateOperationAuthority(command, user, translator());

    }

    /**
     * テナントロールを削除します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @param user     認証ユーザ
     * @throws DomainException ビジネスロジック
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{roleId}")
    public void deleteTenantRole(@PathVariable String tenantId, @PathVariable String roleId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        this.tenantRoleApplicationService.deleteRole(tenantId, user, roleId);
    }

    private ApplicationTranslator<TenantRole, TenantRoleResource> translator() {
        return d -> {
            //@formatter:off
            List<RoleOperationResource> roleOperations = d.getRoleOperations().entrySet().stream()
                    .map(entry -> {
                        return RoleOperationResource.builder()
                                .operationId(entry.getValue().getOperationId().toString())
                                .roleId(entry.getValue().getRoleId().toString())
                                .build();
                    })
                    .toList();
            return TenantRoleResource.builder()
                    .tenantId(d.getTenantId().toString())
                    .roleId(d.getRoleId().toString())
                    .operations(roleOperations)
                    .build();
            //@formatter:on
        };
    }
}
