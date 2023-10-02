package org.nortis.infrastructure.security.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.AdminFlg;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * テナントのAPIキーで認証したユーザの{@link NortisUserDetails}の実装クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class TenantNortisUser extends NortisUser {

    private static final long serialVersionUID = 1L;

    @Getter
    private final Tenant tenant;

    @Getter
    private final Authentication authentication;

    /**
     * インスタンスを生成します
     * 
     * @param tenant         テナント
     * @param authentication 認証
     * @param expired        有効期限切れかどうか
     * @param authorities    権限
     */
    public TenantNortisUser(Tenant tenant, Authentication authentication, boolean expired,
            Collection<? extends GrantedAuthority> authorities) {
        super(tenant.getTenantId(), tenant.getTenantId().toString(), authentication.getApiKey().toString(), expired,
                authorities);
        this.tenant = tenant;
        this.authentication = authentication;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean hasAuthorityOf(Tenant tenant, OperationId operation) {
        // テナントAPIキーのユーザは全ての操作を許可される
        return this.tenant.getTenantId().equals(tenant.getTenantId());
    }

    @Override
    public boolean isJoinTenantOf(TenantId tenantId) {
        return this.tenant.getTenantId().equals(tenantId);
    }

    @Override
    public boolean isTenant() {
        return true;
    }

    @Override
    public boolean isUser() {
        return false;
    }

    /**
     * テナントの{@link NortisUser}を作成します
     * 
     * @param authentication 認証
     * @param tenant         テナント
     * @param expired        期限切れかどうか
     * @return {@link NortisUser}
     */
    public static TenantNortisUser createOfTenant(Authentication authentication, Tenant tenant, boolean expired) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(AdminFlg.MEMBER.name()));
        return new TenantNortisUser(tenant, authentication, expired, authorities);
    }

}
