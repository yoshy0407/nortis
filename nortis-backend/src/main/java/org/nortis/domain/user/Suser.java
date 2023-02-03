package org.nortis.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.utils.RandomString;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;


/**
 * ユーザ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "SUSER")
@Entity(metamodel = @Metamodel)
public class Suser {

	@Id
	@Column(name = "USER_ID")
	private UserId userId;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "ENCODED_PASSWORD")
	private String encodedPassword;

	@Column(name = "ADMIN_FLG")
	private AdminFlg adminFlg;

	@Transient
	private final List<TenantUser> tenantUserList = new ArrayList<>();

	@Column(name = "LOGIN_FLG")
	private LoginFlg loginFlg;

	@Column(name = "CREATE_ID")
	private String createId;

	@Column(name = "CREATE_DT")
	private LocalDateTime createDt;

	@Setter
	@Column(name = "UPDATE_ID")
	private String updateId;

	@Setter
	@Column(name = "UPDATE_DT")
	private LocalDateTime updateDt;

	@Setter
	@Version
	@Column(name = "VERSION")
	private long version;

	/**
	 * ユーザ名を変更します
	 * @param username ユーザ名
	 * @param userId 更新者ID
	 */
	public void changeUsername(String username, String userId) {
		setUsername(username);
		setUpdateId(userId);
		setUpdateDt(LocalDateTime.now());
	}

	/**
	 * パスワードを変更します
	 * @param password パスワード
	 * @param userId 更新者ID
	 */
	public void changePassword(String password, String userId) {
		setPassword(password);
		setUpdateId(userId);
		setUpdateDt(LocalDateTime.now());
	}

	/**
	 * パスワードをリセットします
	 * @param userId ユーザID
	 * @return リセット後のパスワード
	 */
	public String resetPassword(String userId) {
		String password = RandomString.of(15).build();
		changePassword(
				password,
				userId);
		return password;
	}

	/**
	 * ログインを実施します
	 * @return ログインで発行した認証オブジェクト
	 */
	public Authentication login() {
		setLoginFlg(LoginFlg.LOGIN);
		setUpdateId(this.userId.toString());
		setUpdateDt(LocalDateTime.now());
		return Authentication.createFromUserId(this.userId);
	}

	/**
	 * ログアウトします
	 */
	public void logout() {
		setLoginFlg(LoginFlg.NOT_LOGIN);
		setUpdateId(this.updateId);
		setUpdateDt(LocalDateTime.now());
	}

	/**
	 * ユーザIDを設定します
	 * @param userId ユーザID
	 */
	public void setUserId(UserId userId) {
		Validations.notNull(userId, "ユーザID");
		this.userId = userId;
	}

	/**
	 * APIキーを作成します
	 * @return 認証オブジェクト
	 */
	public Authentication createApiKey() {
		return Authentication.createFromUserId(this.userId);
	}

	/**
	 * テナント権限を追加します
	 * @param tenantId テナントID
	 */
	public void grantTenantAccressOf(TenantId tenantId) {
		Optional<TenantUser> result = this.tenantUserList.stream()
				.filter(tenant -> tenant.getTenantId().equals(tenantId))
				.findFirst();
		if (result.isPresent()) {
			throw new DomainException("MSG50001", tenantId.toString());
		}
		TenantUser tenantUser = new TenantUser(this.userId, tenantId);
		tenantUser.setInsert(true);
		this.tenantUserList.add(tenantUser);
	}

	/**
	 * テナント権限を削除します
	 * @param tenantId テナントID
	 */
	public void revokeTenantAccessOf(TenantId tenantId) {
		Optional<TenantUser> result = this.tenantUserList.stream()
				.filter(tenant -> tenant.getTenantId().equals(tenantId))
				.findFirst();
		if (result.isEmpty()) {
			throw new DomainException("MSG50002", tenantId.toString());
		}
		result.get().setDeleted(true);
	}

	/**
	 * ユーザ名を設定します
	 * @param username ユーザ名
	 */
	public void setUsername(String username) {
		Validations.hasText(username, "ユーザ名");
		Validations.maxTextLength(username, 50, "ユーザ名");
		this.username = username;
	}

	/**
	 * パスワードを設定します
	 * @param encodedPassword パスワード
	 */
	public void setEncodedPassword(String encodedPassword) {
		Validations.hasText(encodedPassword, "パスワード");
		this.encodedPassword = encodedPassword;
	}
	
	/**
	 * パスワードを設定します
	 * @param password パスワード
	 */
	protected void setPassword(String password) {
		Validations.hasText(password, "パスワード");
		String encodedPassword = ApplicationContextAccessor.getPasswordEncoder().encode(password);
		setEncodedPassword(encodedPassword);
	}

	/**
	 * 管理者フラグを設定します
	 * @param adminFlg 管理者フラグ
	 */
	public void setAdminFlg(AdminFlg adminFlg) {
		Validations.notNull(adminFlg, "管理者フラグ");
		this.adminFlg = adminFlg;
	}

	/**
	 * ログインフラグを設定します
	 * @param loginFlg ログインフラグ
	 */
	public void setLoginFlg(LoginFlg loginFlg) {
		Validations.notNull(loginFlg, "ログインフラグ");
		this.loginFlg = loginFlg;
	}

	/**
	 * 作成者IDを設定します
	 * @param createId 作成者ID
	 */
	public void setCreateId(String createId) {
		Validations.hasText(encodedPassword, "作成者ID");
		this.createId = createId;
	}

	/**
	 * 作成時刻を設定します
	 * @param createDt 作成時刻
	 */
	public void setCreateDt(LocalDateTime createDt) {
		Validations.notNull(createDt, "作成時刻");
		this.createDt = createDt;
	}

	/**
	 * 通常ユーザを作成します
	 * @param userId ユーザID
	 * @param username ユーザ名
	 * @param password パスワード
	 * @param tenantIds 紐付けるテナントのID
	 * @param createUserId 作成者ID
	 * @return ユーザ
	 */
	public static Suser createMember(
			UserId userId,
			String username,
			String password,
			List<TenantId> tenantIds,
			String createUserId) {
		Suser suser = new Suser();
		suser.setUserId(userId);
		suser.setUsername(username);
		suser.setPassword(password);
		suser.setAdminFlg(AdminFlg.MEMBER);
		suser.setLoginFlg(LoginFlg.NOT_LOGIN);
		suser.setCreateId(createUserId);
		suser.setCreateDt(LocalDateTime.now());
		suser.setVersion(1L);
		tenantIds.forEach(id -> {
			suser.getTenantUserList().add(new TenantUser(userId, id));
		});
		return suser;
	}

	/**
	 * 管理者ユーザを作成します
	 * @param userId ユーザID
	 * @param username ユーザ名
	 * @param password パスワード
	 * @param createUserId 作成者ID
	 * @return ユーザ
	 */
	public static Suser createAdmin(
			UserId userId,
			String username,
			String password,
			String createUserId) {
		Suser suser = new Suser();
		suser.setUserId(userId);
		suser.setUsername(username);
		suser.setPassword(password);
		suser.setAdminFlg(AdminFlg.ADMIN);
		suser.setLoginFlg(LoginFlg.NOT_LOGIN);
		suser.setCreateId(createUserId);
		suser.setCreateDt(LocalDateTime.now());
		suser.setVersion(1L);
		return suser;
	}
}
