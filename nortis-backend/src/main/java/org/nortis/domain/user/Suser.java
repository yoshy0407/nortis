package org.nortis.domain.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.Getter;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginFlg;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.doma.NortisEntityListener;
import org.nortis.infrastructure.doma.entity.RootEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * ユーザ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "SUSER")
@Entity(metamodel = @Metamodel, listener = NortisEntityListener.class)
public class Suser extends RootEntity {

    @Id
    @Column(name = "USER_ID")
    private UserId userId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "LOGIN_ID")
    private LoginId loginId;

    @Column(name = "HASHED_PASSWORD")
    private HashedPassword hashedPassword;

    @Column(name = "LOGIN_FLG")
    private LoginFlg loginFlg;

    @Column(name = "ADMIN_FLG")
    private AdminFlg adminFlg;

    @Transient
    private final List<UserRole> userRoles = new ArrayList<>();

    /**
     * ユーザIDを設定します
     * 
     * @param userId ユーザID
     * @throws DomainException ドメインロジックエラー
     */
    public void setUserId(UserId userId) throws DomainException {
        Validations.notNull(userId, "ユーザID");
        this.userId = userId;
    }

    /**
     * ユーザ名を設定します
     * 
     * @param username ユーザ名
     * @throws DomainException ドメインロジックエラー
     */
    public void setUsername(String username) throws DomainException {
        Validations.hasText(username, "ユーザ名");
        Validations.maxTextLength(username, 50, "ユーザ名");
        this.username = username;
    }

    /**
     * set ログインID
     * 
     * @param loginId ログインID
     * @throws DomainException バリデーションエラー
     */
    public void setLoginId(LoginId loginId) throws DomainException {
        Validations.notNull(loginId, "ログインID");
        this.loginId = loginId;
    }

    /**
     * set password
     * 
     * @param hashedPassword ハッシュ化されたパスワード
     * @throws DomainException バリデーションエラー
     */
    public void setHashedPassword(HashedPassword hashedPassword) throws DomainException {
        Validations.notNull(hashedPassword, "パスワード");
        this.hashedPassword = hashedPassword;
    }

    /**
     * set loginFlg
     * 
     * @param loginFlg ログインフラグ
     * @throws DomainException ビジネスロジックエラー
     */
    public void setLoginFlg(LoginFlg loginFlg) throws DomainException {
        Validations.notNull(hashedPassword, "ログインフラグ");
        this.loginFlg = loginFlg;
    }

    /**
     * 管理者フラグを設定します
     * 
     * @param adminFlg 管理者フラグ
     * @throws DomainException ドメインロジックエラー
     */
    public void setAdminFlg(AdminFlg adminFlg) throws DomainException {
        Validations.notNull(adminFlg, "管理者フラグ");
        this.adminFlg = adminFlg;
    }

    /**
     * ユーザ名を変更します
     * 
     * @param username ユーザ名
     * @throws DomainException ドメインロジックエラー
     */
    public void changeUsername(String username) throws DomainException {
        setUsername(username);
    }

    /**
     * パスワードを変更します
     * 
     * @param hashedPassword ハッシュ化されたパスワード
     * @throws DomainException ドメインロジックエラー
     */
    public void changePasswordOf(HashedPassword hashedPassword) throws DomainException {
        setHashedPassword(hashedPassword);
    }

    /**
     * APIキーを作成します
     * 
     * @return 認証オブジェクト
     * @throws DomainException ドメインロジックエラー
     */
    public Authentication login() throws DomainException {
        setLoginFlg(LoginFlg.LOGIN);
        return Authentication.createFromUserId(userId);
    }

    /**
     * ログアウトします
     * 
     * @throws DomainException ドメインロジックエラー
     */
    public void logout() throws DomainException {
        setLoginFlg(LoginFlg.NOT_LOGIN);
    }

    /**
     * テナントに参加しているか確認します
     * 
     * @param tenantId テナントID
     * @return テナントに参加しているか確認します
     */
    public boolean isJoinTenantOf(TenantId tenantId) {
        // 管理者はどのテナントでもアクセス化
        if (this.adminFlg.equals(AdminFlg.ADMIN)) {
            return true;
        }
        //@formatter:off
        return this.userRoles.stream()
                .filter(tenant -> tenant.getTenantId().equals(tenantId))
                .findFirst().isPresent();
        //@formatter:on
    }

    /**
     * テナントのロールを返却します
     * 
     * @param tenantId テナントID
     * @return ロールのリスト
     */
    public List<RoleId> getHasRoleOf(TenantId tenantId) {
        //@formatter:off
        return this.userRoles.stream()
                .filter(authority -> authority.getTenantId().equals(tenantId))
                .map(authority -> authority.getRoleId())
                .toList();
        //@formatter:on
    }

    /**
     * 管理者ユーザかどうか確認します
     * 
     * @return 管理者ユーザかどうか
     */
    public boolean isAdmin() {
        return this.adminFlg.equals(AdminFlg.ADMIN);
    }

    /**
     * テナント権限を追加します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @throws DomainException ドメインロジックエラー
     */
    public void grantTenantAccessOf(TenantId tenantId, RoleId roleId) throws DomainException {
        Optional<UserRole> result = this.userRoles.stream().filter(tenant -> tenant.getTenantId().equals(tenantId))
                .findFirst();
        if (result.isPresent()) {
            // 冪等に処理するため何もせずに終了
            return;
        }
        UserRole userRole = new UserRole(this.userId, tenantId, roleId,
                SecurityContextUtils.getCurrentAuthorizedId().toString(), LocalDateTime.now());
        userRole.setInsert();
        this.userRoles.add(userRole);
    }

    /**
     * テナント権限を削除します
     * 
     * @param tenantId テナントID
     * @param roleId   ロールID
     * @throws DomainException ドメインロジックエラー
     */
    public void revokeTenantAccessOf(TenantId tenantId, RoleId roleId) throws DomainException {
        //@formatter:off
        Optional<UserRole> result = this.userRoles.stream()
                .filter(tenant -> {
                    return tenant.getTenantId().equals(tenantId) && tenant.getRoleId().equals(roleId);
                })
                .findFirst();
        //@formatter:on
        if (result.isEmpty()) {
            // 冪等に処理するため何もせずに終了
            return;
        }
        result.get().setDelete();
    }

    /**
     * ユーザを作成します
     * 
     * @param userId            ユーザID
     * @param username          ユーザ名
     * @param adminFlg          管理者フラグ
     * @param tenantAuthorities 紐付けるテナントのIDとロール
     * @param loginId           ログインID
     * @param hashedPassword    ハッシュ化されたパスワード
     * @return ユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public static Suser create(UserId userId, String username, AdminFlg adminFlg,
            Map<TenantId, RoleId> tenantAuthorities, LoginId loginId, HashedPassword hashedPassword)
            throws DomainException {
        Suser suser = new Suser();
        suser.setUserId(userId);
        suser.setUsername(username);
        suser.setLoginId(loginId);
        suser.setHashedPassword(hashedPassword);
        suser.setLoginFlg(LoginFlg.NOT_LOGIN);
        suser.setAdminFlg(adminFlg);
        for (Entry<TenantId, RoleId> entry : tenantAuthorities.entrySet()) {
            suser.grantTenantAccessOf(entry.getKey(), entry.getValue());
        }

        return suser;
    }

}
