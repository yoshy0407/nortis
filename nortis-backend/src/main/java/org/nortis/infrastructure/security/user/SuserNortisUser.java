package org.nortis.infrastructure.security.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.OperationId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * ユーザでログインした{@link NortisUserDetails}の実装クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class SuserNortisUser extends NortisUser {

    private static final long serialVersionUID = 1L;

    @Getter
    private final Suser suser;

    @Getter
    private final Authentication authentication;

    /**
     * インスタンスを生成します
     * 
     * @param suser          ユーザ
     * @param authentication 認証
     * @param expired        有効期間切れか
     * @param authorities    権限
     */
    public SuserNortisUser(Suser suser, Authentication authentication, boolean expired,
            Collection<? extends GrantedAuthority> authorities) {
        super(suser.getUserId(), suser.getUserId().toString(), authentication.getApiKey().toString(), expired,
                authorities);
        this.suser = suser;
        this.authentication = authentication;
    }

    @Override
    public boolean isAdmin() {
        return this.suser.isAdmin();
    }

    @Override
    public boolean hasAuthorityOf(Tenant tenant, OperationId operation) {
        return tenant.permitOperation(suser, operation);
    }

    @Override
    public boolean isJoinTenantOf(TenantId tenantId) {
        return this.suser.isJoinTenantOf(tenantId);
    }

    @Override
    public boolean isTenant() {
        return false;
    }

    @Override
    public boolean isUser() {
        return true;
    }

    /**
     * ユーザの{@link NortisUser}を作成します
     * 
     * @param authentication 認証
     * @param suser          ユーザ
     * @param expired        期限切れかどうか
     * @return {@link NortisUser}
     */
    public static SuserNortisUser createOfUser(Suser suser, Authentication authentication, boolean expired) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(suser.getAdminFlg().name()));
        return new SuserNortisUser(suser, authentication, expired, authorities);
    }
}
