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
	
	private final boolean isTenant;
	
	/**
	 * インスタンスを生成します
	 * @param usernane ユーザ
	 * @param tenantIds テナントID
	 * @param password パスワード
	 * @param expired 期限切れかどうか
	 * @param authorities 権限
	 * @param isTenant テナントかどうか
	 */
	public NortisUser(
			String usernane, 
			String[] tenantIds,
			String password, 
			boolean expired,
			Collection<? extends GrantedAuthority> authorities,
			boolean isTenant) {
		super(usernane, password, true, true, !expired, true, authorities);
		this.tenantIds = tenantIds;
		this.isTenant = isTenant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getTenantId() {
		return this.tenantIds;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		//Spotbugでエラーになるので、オーバーライドする
		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		//Spotbugでエラーになるので、オーバーライドする
		return super.hashCode();
	}

	/**
	 * テナントの{@link NortisUser}を作成します
	 * @param authentication 認証
	 * @param expired 期限切れかどうか
	 * @return {@link NortisUser}
	 */
	public static NortisUser createOfTenant(Authentication authentication, boolean expired) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(AdminFlg.MEMBER.name()));
		return new NortisUser(
				authentication.getTenantId().toString(), 
				new String[] { authentication.getTenantId().toString() },
				authentication.getApiKey().toString(), 
				expired,
				authorities,
				true);
	}
	
	/**
	 * ユーザの{@link NortisUser}を作成します
	 * @param authentication 認証
	 * @param suser ユーザ
	 * @param expired 期限切れかどうか
	 * @return {@link NortisUser}
	 */
	public static NortisUser createOfUser(Authentication authentication, Suser suser, boolean expired) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(suser.getAdminFlg().name()));
		String[] tenantIds = suser.getTenantUserList().stream()
				.map(t -> t.getTenantId().toString())
				.toArray(String[]::new);
		return new NortisUser(
				authentication.getUserId().toString(), 
				tenantIds,
				authentication.getApiKey().toString(), 
				expired,
				authorities,
				false);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTenant() {
		return this.isTenant;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUser() {
		return !this.isTenant;
	}
	
}
