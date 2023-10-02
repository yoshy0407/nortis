package org.nortis.port.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.application.tenant.TenantApplicationService;
import org.nortis.application.tenant.model.TenantNameUpdateCommand;
import org.nortis.application.tenant.model.TenantRegisterCommand;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.port.rest.payload.TenantPatchRequest;
import org.nortis.port.rest.payload.TenantRequest;
import org.nortis.port.rest.resource.ApiKeyResource;
import org.nortis.port.rest.resource.TenantResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
 * テナントのエンドポイントのコントローラです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RequestMapping("/tenant")
@RestController
public class TenantRestController {

    private final TenantApplicationService tenantApplicationService;

    /**
     * テナント情報を返却します
     * 
     * @param tenantId テナントID
     * @param user     認証済みユーザ
     * @return テナントリソース
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{tenantId}")
    public TenantResource get(@PathVariable(name = "tenantId", required = true) String tenantId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.tenantApplicationService.getTenant(tenantId, user, translator());
    }

    /**
     * 全てのテナントを返却します
     * 
     * @param user 認証ユーザ
     * @return テナントリソースのリスト
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TenantResource> getList(@AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.tenantApplicationService.getAllTenant(translator(), user);
    }

    /**
     * テナントを作成します
     * 
     * @param req         リクエストボディ
     * @param userDetails 認証済みユーザ
     * @return 作成したテナントリソース
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public TenantResource register(@RequestBody TenantRequest req,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        TenantRegisterCommand command = new TenantRegisterCommand(req.tenantId(), req.tenantName());
        return this.tenantApplicationService.register(command, userDetails, translator());
    }

    /**
     * APIキーを作成します
     * 
     * @param tenantId    テナントID
     * @param userDetails ユーザ
     * @return APIキーのリソース
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("{tenantId}/apiKey")
    public ApiKeyResource createApiKey(@PathVariable String tenantId,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        ApiKey apiKey = this.tenantApplicationService.createApiKey(tenantId, userDetails);
        return new ApiKeyResource(apiKey.toString());
    }

    /**
     * テナントを更新します
     * 
     * @param tenantId    テナントID
     * @param req         リクエストボディ
     * @param userDetails 認証済みのユーザ
     * @return 変更したテナントリソース
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{tenantId}")
    public TenantResource updateName(@PathVariable(name = "tenantId", required = true) String tenantId,
            @RequestBody TenantPatchRequest req, @AuthenticationPrincipal NortisUserDetails userDetails)
            throws DomainException {
        TenantNameUpdateCommand command = new TenantNameUpdateCommand(tenantId, req.name());
        return this.tenantApplicationService.changeName(command, userDetails, translator());
    }

    /**
     * テナントを削除します
     * 
     * @param tenantId    テナントID
     * @param userDetails 認証済みユーザ
     * @throws DomainException ドメインロジックエラー
     */
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("{tenantId}")
    public void delete(@PathVariable(name = "tenantId", required = true) String tenantId,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        this.tenantApplicationService.delete(tenantId, userDetails);
    }

    private ApplicationTranslator<Tenant, TenantResource> translator() {
        return data -> {
            return new TenantResource(data.getTenantId().toString(), data.getTenantName());
        };
    }
}
