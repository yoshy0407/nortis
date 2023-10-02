package org.nortis.infrastructure.security.user;

import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.security.Identity;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * このアプリケーションで利用する{@link UserDetails}の拡張インタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface NortisUserDetails extends UserDetails {

    /**
     * IDを返却します
     * 
     * @return ID
     */
    public Identity getIdentity();

    /**
     * 管理者ユーザかチェックします
     * 
     * @return 管理者ユーザかどうか
     */
    public boolean isAdmin();

    /**
     * テナントの権限を持っているかチェックします
     * 
     * @param tenant    テナント
     * @param operation 操作内容
     * @return テナントの権限を持っているか
     */
    public boolean hasAuthorityOf(Tenant tenant, OperationId operation);

    /**
     * テナントに参加しているかどうかチェックします
     * 
     * @param tenantId テナンtのID
     * @return テナントに参加しているかどうか
     */
    public boolean isJoinTenantOf(TenantId tenantId);

    /**
     * テナント自身であるかどうかチェックします
     * 
     * @return テナント自身かどうか
     */
    public boolean isTenant();

    /**
     * ユーザであるかどうかチェックします
     * 
     * @return ユーザであるかどうか
     */
    public boolean isUser();

}
