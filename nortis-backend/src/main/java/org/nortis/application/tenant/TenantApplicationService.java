package org.nortis.application.tenant;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.application.tenant.model.TenantNameUpdateCommand;
import org.nortis.application.tenant.model.TenantRegisterCommand;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.service.TenantDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;

/**
 * テナントのアプリケーションサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class TenantApplicationService {

    /** テナントリポジトリ */
    private final TenantRepository tenantRepository;

    private final AuthenticationRepository authenticationRepository;

    private final TenantDomainService tenantDomainService;

    private final NumberingDomainService numberingDomainService;

    /** 権限チェックのドメインサービス */
    private final AuthorityCheckDomainService authorityCheckDomainService;

    /**
     * テナントを取得します
     * 
     * @param <R>         結果クラス
     * @param rawTenantId テナントIDの文字列
     * @param user        ユーザ
     * @param translator  トランスレータ
     * @return テナント
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R getTenant(String rawTenantId, NortisUserDetails user, ApplicationTranslator<Tenant, R> translator)
            throws DomainException {

        TenantId tenantId = TenantId.create(rawTenantId);

        // 権限チェック
        this.authorityCheckDomainService.checkJoinedTenantOf(user, tenantId);

        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }

        return translator.translate(optTenant.get());
    }

    /**
     * 全てのテナントを取得します
     * 
     * @param <R>         結果
     * @param translator  変換処理
     * @param userDetails 認証ユーザ
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> List<R> getAllTenant(ApplicationTranslator<Tenant, R> translator, NortisUserDetails userDetails)
            throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(userDetails);

        List<Tenant> allTenant = this.tenantRepository.getAllTenant();

        //@formatter:off
        return allTenant.stream()
                .map(tenant -> translator.translate(tenant))
                .toList();
        //@formatter:on
    }

    /**
     * テナントを登録します
     * 
     * @param <R>         結果クラス
     * @param command     登録コマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R register(TenantRegisterCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Tenant, R> translator) throws DomainException {

        // 権限のチェック
        this.authorityCheckDomainService.checkAdminOf(userDetails);

        TenantId tenantId = this.numberingDomainService.createNewTenantId();
        TenantIdentifier tenantIdentifier = TenantIdentifier.create(command.tenantIdentifier());

        this.tenantDomainService.beforeRegisterCheck(tenantId, tenantIdentifier);

        Tenant tenant = tenantDomainService.createTenant(tenantId, tenantIdentifier, command.name());
        this.tenantRepository.save(tenant);

        return translator.translate(tenant);
    }

    /**
     * テナントのAPIキーを作成します
     * 
     * @param rawTenantId テナントID
     * @param user        ユーザ
     * @return APIキー
     * @throws DomainException ドメインロジックエラー
     */
    public ApiKey createApiKey(String rawTenantId, NortisUserDetails user) throws DomainException {
        TenantId tenantId = TenantId.create(rawTenantId);

        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }

        Tenant tenant = optTenant.get();
        // 権限チェック
        this.authorityCheckDomainService.checkHasCreateApiKeyOf(user, tenant);

        Optional<Authentication> optAuth = this.authenticationRepository.getFromTenantId(tenant.getTenantId());

        optAuth.ifPresent(data -> {
            this.authenticationRepository.remove(data);
        });

        Authentication auth = tenant.createApiKey();

        this.authenticationRepository.save(auth);
        return auth.getApiKey();
    }

    /**
     * テナント名を変更します
     * 
     * @param <R>         結果クラス
     * @param command     テナント名更新のコマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R changeName(TenantNameUpdateCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Tenant, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());

        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }

        Tenant tenant = optTenant.get();

        // 権限チェック
        this.authorityCheckDomainService.checkHasChangeTenantNameOf(userDetails, tenant);

        tenant.changeTenantName(command.name());

        this.tenantRepository.update(tenant);

        return translator.translate(tenant);
    }

    /**
     * テナントを削除します
     * 
     * @param rawTenantId テナントID
     * @param user        ユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public void delete(String rawTenantId, NortisUserDetails user) throws DomainException {
        TenantId tenantId = TenantId.create(rawTenantId);

        this.authorityCheckDomainService.checkAdminOf(user);

        Optional<Tenant> optTenant = this.tenantRepository.getByTenantId(tenantId);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }
        Tenant tenant = optTenant.get();

        this.tenantRepository.remove(tenant);
    }
}
