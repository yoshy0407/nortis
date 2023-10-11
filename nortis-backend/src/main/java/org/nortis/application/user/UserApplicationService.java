package org.nortis.application.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.application.user.model.SuserChangeNameCommand;
import org.nortis.application.user.model.SuserGrantRoleCommand;
import org.nortis.application.user.model.SuserRevokeRoleCommand;
import org.nortis.application.user.model.TenantRoleModel;
import org.nortis.application.user.model.UserRegisterCommand;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.service.PasswordDomainService;
import org.nortis.domain.service.SuserDomainService;
import org.nortis.domain.service.TenantDomainService;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;

/**
 * ユーザのアプリケーションサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class UserApplicationService {

    private final AuthorityCheckDomainService authorityCheckDomainService;

    private final SuserRepository suserRepository;

    private final SuserDomainService suserDomainService;

    private final PasswordDomainService passwordDomainService;

    private final NumberingDomainService numberingDomainService;

    private final TenantDomainService tenantDomainService;

    /**
     * ユーザを取得します
     * 
     * @param <R>        結果クラス
     * @param rawUserId  ユーザID
     * @param user       認証ユーザ
     * @param translator 変換処理
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R getUser(String rawUserId, NortisUserDetails user, ApplicationTranslator<Suser, R> translator)
            throws DomainException {

        UserId userId = UserId.create(rawUserId);

        this.authorityCheckDomainService.checkAccressUser(user, userId);

        Optional<Suser> optUser = this.suserRepository.getByUserId(userId);
        if (optUser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }
        return translator.translate(optUser.get());
    }

    /**
     * ページングで取得します
     * 
     * @param <R>        結果
     * @param paging     ページング
     * @param user       認証ユーザ
     * @param translator 変換処理
     * @return 結果のリスト
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> List<R> getList(Paging paging, NortisUserDetails user, ApplicationTranslator<Suser, R> translator)
            throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(user);

        List<Suser> userList = this.suserRepository.getListPaging(paging);

        //@formatter:off
        return userList.stream()
                .map(data -> translator.translate(data))
                .toList();
        //@formatter:on
    }

    /**
     * ユーザを登録します
     * 
     * @param <R>        結果クラス
     * @param command    ユーザ登録コマンド
     * @param user       ユーザ
     * @param translator 結果変換クラス
     * @return 結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R registerUser(UserRegisterCommand command, NortisUserDetails user,
            ApplicationTranslator<Suser, R> translator) throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(user);

        LoginId loginId = LoginId.create(command.loginId());
        UserId userId = this.numberingDomainService.createNewUserId();

        this.suserDomainService.beforeRegisterCheck(userId, loginId);

        // ロールの整理
        Map<TenantId, RoleId> tenantRoles = new HashMap<>();
        for (TenantRoleModel tenantRole : command.tenantRoleList()) {
            TenantId tenantId = TenantId.create(tenantRole.tenantId());
            RoleId roleId = RoleId.create(tenantRole.roleId());
            this.tenantDomainService.checkExistRole(tenantId, roleId);
            tenantRoles.put(tenantId, roleId);
        }

        this.passwordDomainService.checkPolicyOf(command.password());
        HashedPassword hashedPassword = this.passwordDomainService.hashPassword(command.password());

        AdminFlg adminFlg = AdminFlg.resolve(command.adminFlg().toString());

        Suser suser = Suser.create(userId, command.username(), adminFlg, tenantRoles, loginId, hashedPassword);
        this.suserRepository.save(suser);

        return translator.translate(suser);
    }

    /**
     * ユーザ名を変更します
     * 
     * @param <R>        結果のクラス
     * @param command    ユーザ名変更コマンド
     * @param user       認証ユーザ
     * @param translator 結果変換クラス
     * @return 結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R changeName(SuserChangeNameCommand command, NortisUserDetails user,
            ApplicationTranslator<Suser, R> translator) throws DomainException {
        UserId userId = UserId.create(command.userId());

        authorityCheckDomainService.checkAccressUser(user, userId);

        Optional<Suser> optSuser = this.suserRepository.getByUserId(userId);
        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }

        Suser suser = optSuser.get();
        suser.changeUsername(command.usernane());

        this.suserRepository.update(suser);
        return translator.translate(suser);
    }

    /**
     * パスワードの変更
     * 
     * @param rawUserId         ユーザID
     * @param changeRawPassword パスワード
     * @param user              認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    public void changePassword(String rawUserId, String changeRawPassword, NortisUserDetails user)
            throws DomainException {

        UserId userId = UserId.create(rawUserId);

        this.authorityCheckDomainService.checkSameUser(user, userId);

        Optional<Suser> optSuser = suserRepository.getByUserId(userId);

        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }

        this.passwordDomainService.checkPolicyOf(changeRawPassword);
        HashedPassword hashedPassword = this.passwordDomainService.hashPassword(changeRawPassword);

        Suser suser = optSuser.get();
        suser.changePasswordOf(hashedPassword);

        this.suserRepository.update(suser);

    }

    /**
     * パスワードをリセットします
     * 
     * @param rawUserId ユーザID
     * @param user      認証ユーザ
     * @return リセットしたパスワード
     * @throws DomainException ビジネスロジックエラー
     */
    public String resetPassword(String rawUserId, NortisUserDetails user) throws DomainException {

        UserId userId = UserId.create(rawUserId);

        authorityCheckDomainService.checkAccressUser(user, userId);

        Optional<Suser> optSuser = suserRepository.getByUserId(userId);

        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }

        String resetPassword = this.passwordDomainService.createResetPassword();
        HashedPassword hashedPassword = this.passwordDomainService.hashPassword(resetPassword);

        Suser logins = optSuser.get();
        logins.changePasswordOf(hashedPassword);

        this.suserRepository.update(logins);
        return resetPassword;
    }

    /**
     * テナントの権限をユーザに付与します
     * 
     * @param <R>        結果クラス
     * @param command    コマンド
     * @param user       認証ユーザ
     * @param translator 変換クラス
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R grantRole(SuserGrantRoleCommand command, NortisUserDetails user,
            ApplicationTranslator<Suser, R> translator) throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(user);

        UserId userId = UserId.create(command.userId());
        Optional<Suser> optSuser = this.suserRepository.getByUserId(userId);
        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }

        Suser suser = optSuser.get();
        for (TenantRoleModel role : command.tenantRoleList()) {

            TenantId tenantId = TenantId.create(role.tenantId());
            RoleId roleId = RoleId.create(role.roleId());

            this.tenantDomainService.checkExistRole(tenantId, roleId);

            suser.grantTenantAccessOf(tenantId, roleId);
        }

        this.suserRepository.update(suser);
        return translator.translate(suser);
    }

    /**
     * テナントの権限をユーザに付与します
     * 
     * @param <R>        結果クラス
     * @param command    コマンド
     * @param user       認証ユーザ
     * @param translator 変換クラス
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R revokeRole(SuserRevokeRoleCommand command, NortisUserDetails user,
            ApplicationTranslator<Suser, R> translator) throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(user);

        UserId userId = UserId.create(command.userId());
        Optional<Suser> optSuser = this.suserRepository.getByUserId(userId);
        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }

        Suser suser = optSuser.get();
        for (TenantRoleModel role : command.tenantRoleList()) {

            TenantId tenantId = TenantId.create(role.tenantId());
            RoleId roleId = RoleId.create(role.roleId());

            this.tenantDomainService.checkExistRole(tenantId, roleId);

            suser.revokeTenantAccessOf(tenantId, roleId);
        }

        this.suserRepository.update(suser);
        return translator.translate(suser);
    }

    /**
     * 削除されたロールIDに紐づくロールが付与されているユーザから、権限を剥奪します
     * 
     * @param rawRoleId ロールID
     * @throws DomainException ビジネスロジックエラー
     */
    public void revokeRoleFromRoleDeleted(String rawRoleId) throws DomainException {
        RoleId roleId = RoleId.create(rawRoleId);

        List<Suser> userList = this.suserRepository.getFromRoleId(roleId);
        for (Suser user : userList) {
            user.revokeFromRoleId(roleId);
            // 都度更新で良いかどうか微妙・・・
            this.suserRepository.update(user);
        }
    }

    /**
     * ユーザを削除します
     * 
     * @param rawUserId ユーザID
     * @param user      認証ユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public void deleteUserOf(String rawUserId, NortisUserDetails user) throws DomainException {

        this.authorityCheckDomainService.checkAdminOf(user);

        UserId userId = UserId.create(rawUserId);
        Optional<Suser> optSuser = this.suserRepository.getByUserId(userId);
        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }
        this.suserRepository.remove(optSuser.get());
    }

}
