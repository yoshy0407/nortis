package org.nortis.infrastructure.security.user;

import java.util.Collection;
import org.nortis.infrastructure.security.Identity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * テナントのAPIキー認証に対する{@link NortisUserDetails}の実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public abstract class NortisUser extends User implements NortisUserDetails {

    private static final long serialVersionUID = 1L;

    private final Identity identity;

    /**
     * インスタンスを生成します
     * 
     * @param identity    ID
     * @param usernane    ユーザ
     * @param password    パスワード
     * @param expired     期限切れかどうか
     * @param authorities 権限
     */
    public NortisUser(Identity identity, String usernane, String password, boolean expired,
            Collection<? extends GrantedAuthority> authorities) {
        super(usernane, password, true, true, !expired, true, authorities);
        this.identity = identity;
    }

    @Override
    public Identity getIdentity() {
        return this.identity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        // Spotbugでエラーになるので、オーバーライドする
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // Spotbugでエラーになるので、オーバーライドする
        return super.hashCode();
    }

}
