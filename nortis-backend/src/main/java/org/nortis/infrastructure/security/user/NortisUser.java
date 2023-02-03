package org.nortis.infrastructure.security.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.value.AdminFlg;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * テナントのAPIキー認証に対する{@link NortisUserDetails}の実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class NortisUser extends User implements NortisUserDetails {

	private static final long serialVersionUID = 1L;
	
	private final String[] tenantIds;
	
	/**
	 * インスタンスを生成します
	 * @param usernane ユーザ
	 * @param tenantIds テナントID
	 * @param password パスワード
	 * @param authorities 権限
	 */
	public NortisUser(
			String usernane, 
			String[] tenantIds,
			String password, 
			Collection<? extends GrantedAuthority> authorities) {
		super(usernane, password, authorities);
		this.tenantIds = tenantIds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getTenantId() {
		return this.tenantIds;
	}

	/**
	 * テナントの{@link NortisUser}を作成します
	 * @param authentication 認証
	 * @return {@link NortisUser}
	 */
	public static NortisUser createOfTenant(Authentication authentication) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(AdminFlg.MEMBER.name()));
		return new NortisUser(
				authentication.getTenantId().toString(), 
				new String[] { authentication.getTenantId().toString() },
				authentication.getApiKey().toString(), 
				authorities);
	}
	
	/**
	 * ユーザの{@link NortisUser}を作成します
	 * @param authentication 認証
	 * @param suser ユーザ
	 * @return {@link NortisUser}
	 */
	public static NortisUser createOfUser(Authentication authentication, Suser suser) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(suser.getAdminFlg().name()));
		String[] tenantIds = suser.getTenantUserList().stream()
				.map(t -> t.getTenantId().toString())
				.toArray(String[]::new);
		return new NortisUser(
				authentication.getUserId().toString(), 
				tenantIds,
				authentication.getApiKey().toString(), 
				authorities);		
	}
}
