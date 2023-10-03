package org.nortis.application.tenant;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.Tenant_;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DataNotFoundException;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.seasar.doma.jdbc.criteria.Entityql;

/**
 * テナントのクエリサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class TenantQueryApplicationService {

    private final Entityql entityql;

    private final AuthorityCheckDomainService authorityCheckDomainService;

    private final Tenant_ tenant = new Tenant_();

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

        Optional<Tenant> optTenant = this.entityql.from(this.tenant).where(c -> c.eq(this.tenant.tenantId, tenantId))
                .fetchOptional();

        if (optTenant.isEmpty()) {
            throw new DataNotFoundException(MessageCodes.nortis00003("テナント"));
        }

        return translator.translate(optTenant.get());
    }

    /**
     * 全てのテナントを取得します
     * 
     * @param <R>         結果
     * @param paging      ページング
     * @param translator  変換処理
     * @param userDetails 認証ユーザ
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> List<R> getListTenant(Paging paging, NortisUserDetails userDetails,
            ApplicationTranslator<Tenant, R> translator) throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(userDetails);

        //@formatter:off
        List<Tenant> allTenant = this.entityql.from(this.tenant)
                .offset(paging.offset())
                .limit(paging.limit())
                .fetch();

        return allTenant.stream()
                .map(tenant -> translator.translate(tenant))
                .toList();
        //@formatter:on
    }

}
