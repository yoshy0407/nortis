package org.nortis.domain.service;

import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.exception.NoAuthorityDomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;

/**
 * ユーザの権限をチェックするドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class AuthorityCheckDomainService {

    /**
     * 管理者ユーザであるか確認します
     * 
     * @param nortisUser ユーザ
     * @throws NoAuthorityDomainException 権限エラー
     */
    public void checkAdminOf(NortisUserDetails nortisUser) throws NoAuthorityDomainException {
        if (!nortisUser.isAdmin()) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * 対象のテナントへのアクセス権限があるかどうかチェックします
     * 
     * @param nortisUser ユーザ
     * @param tenantId   テナントID
     * @throws NoAuthorityDomainException ロジックエラー
     */
    public void checkJoinedTenantOf(NortisUserDetails nortisUser, TenantId tenantId) throws NoAuthorityDomainException {
        if (!nortisUser.isJoinTenantOf(tenantId)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * テナント名の変更権限権限を持っているかチェックします
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException ビジネスロジックエラー
     */
    public void checkHasChangeTenantNameOf(NortisUserDetails nortisUser, Tenant tenant)
            throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.WRITE_TENANT_NAME)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * APIキー作成の権限を持っているかチェックします
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException ビジネスロジックエラー
     */
    public void checkHasCreateApiKeyOf(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.WRITE_APIKEY)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * ロールの読み取り権限があるか
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException 認可エラー
     */
    public void checkHasReadRole(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.READ_TENANT_ROLE)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * ロールの作成・変更権限があるか
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException 認可エラー
     */
    public void checkHasWriteRole(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.READ_TENANT_ROLE)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * ロールの読み取り権限があるか
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException 認可エラー
     */
    public void checkHasReadEndpoint(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.READ_ENDPOINT)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * ロールの作成・変更権限があるか
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException 認可エラー
     */
    public void checkHasWriteEndpoint(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.WRITE_ENDPOINT)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * コンシューマの読み取り権限があるか
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException 認可エラー
     */
    public void checkHasReadConsumer(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.READ_CONSUMER)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

    /**
     * コンシューマの更新権限があるか
     * 
     * @param nortisUser ユーザ
     * @param tenant     テナント
     * @throws NoAuthorityDomainException 認可エラー
     */
    public void checkHasWriteConsumer(NortisUserDetails nortisUser, Tenant tenant) throws NoAuthorityDomainException {
        if (!nortisUser.hasAuthorityOf(tenant, OperationId.WRITE_CONSUMER)) {
            throw new NoAuthorityDomainException(MessageCodes.nortis50005());
        }
    }

}
