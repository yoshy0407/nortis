package org.nortis.port.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.application.user.UserApplicationService;
import org.nortis.application.user.model.SuserChangeNameCommand;
import org.nortis.application.user.model.SuserGrantRoleCommand;
import org.nortis.application.user.model.SuserRevokeRoleCommand;
import org.nortis.application.user.model.UserRegisterCommand;
import org.nortis.domain.user.Suser;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.port.rest.payload.UserCreateRequest;
import org.nortis.port.rest.payload.UserPasswordUpdateRequest;
import org.nortis.port.rest.payload.UserRoleRequest;
import org.nortis.port.rest.payload.UsernameUpdateRequest;
import org.nortis.port.rest.resource.ResetPasswordResource;
import org.nortis.port.rest.resource.UserResource;
import org.nortis.port.rest.resource.UserRoleResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザのコントローラ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RequestMapping("/user")
@RestController
public class UserRestController {

    private final UserApplicationService userApplicationService;

    /**
     * 一つのユーザを取得します
     * 
     * @param userId ユーザID
     * @param user   認証ユーザ
     * @return 取得結果
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{userId}")
    public UserResource get(@PathVariable String userId, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        return this.userApplicationService.getUser(userId, user, translator());
    }

    /**
     * 複数ユーザをページングで取得します
     * 
     * @param pageNo      ページ番号
     * @param pagePerSize １ページ当たりの件数
     * @param user        認証済みユーザ
     * @return 取得結果
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserResource> getList(@RequestParam int pageNo, @RequestParam int pagePerSize,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.userApplicationService.getList(new Paging(pageNo, pagePerSize), user, translator());
    }

    /**
     * ユーザを作成します
     * 
     * @param request リクエスト
     * @param user    認証ユーザ
     * @return 作成したユーザのリソース
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public UserResource create(@RequestBody @Validated UserCreateRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        //@formatter:off
        UserRegisterCommand command = UserRegisterCommand.builder()
                .username(request.getUsername())
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .adminFlg(request.isAdminFlg() == true ? 1 : 0)
                .tenantRoleList(request.getRoles())
                .build();
        //@formatter:on

        return this.userApplicationService.registerUser(command, user, translator());
    }

    /**
     * ユーザ名を変更します
     * 
     * @param userId  ユーザID
     * @param request リクエストボディ
     * @param user    認証ユーザ
     * @return レスポンス
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{userId}/usernane")
    public UserResource changeName(@PathVariable String userId, @RequestBody @Validated UsernameUpdateRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        SuserChangeNameCommand command = new SuserChangeNameCommand(userId, request.getUsername());
        return this.userApplicationService.changeName(command, user, translator());
    }

    /**
     * パスワードを変更します
     * 
     * @param userId  ユーザID
     * @param request リクエスト
     * @param user    認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{userId}/password")
    public void changePassword(@PathVariable String userId, @RequestBody @Validated UserPasswordUpdateRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        this.userApplicationService.changePassword(userId, request.getPassword(), user);
    }

    /**
     * パスワードをリセットします
     * 
     * @param userId ユーザID
     * @param user   認証ユーザ
     * @return パスワードリセットリソース
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{userId}/password/reset")
    public ResetPasswordResource resetPassword(@PathVariable String userId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        String password = this.userApplicationService.resetPassword(userId, user);
        return new ResetPasswordResource(password);
    }

    /**
     * ユーザのロールを付与します
     * 
     * @param userId  ユーザID
     * @param request リクエスト
     * @param user    認証ユーザ
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{userId}/role/grant")
    public UserResource grantRole(@PathVariable String userId, @RequestBody @Validated UserRoleRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        SuserGrantRoleCommand command = new SuserGrantRoleCommand(userId, request.getRoles());
        return this.userApplicationService.grantRole(command, user, translator());
    }

    /**
     * ユーザのロールを取り消します
     * 
     * @param userId  ユーザID
     * @param request リクエスト
     * @param user    認証ユーザ
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{userId}/role/revoke")
    public UserResource revokeRole(@PathVariable String userId, @RequestBody @Validated UserRoleRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        SuserRevokeRoleCommand command = new SuserRevokeRoleCommand(userId, request.getRoles());
        return this.userApplicationService.revokeRole(command, user, translator());
    }

    /**
     * ユーザを削除します
     * 
     * @param userId ユーザID
     * @param user   認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{userId}")
    public void delete(@PathVariable String userId, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        this.userApplicationService.deleteUserOf(userId, user);
    }

    private ApplicationTranslator<Suser, UserResource> translator() {
        return suser -> {
            //@formatter:off
            List<UserRoleResource> roles = suser.getUserRoles().stream()
                    .map(data -> new UserRoleResource(data.getTenantId().toString(), data.getRoleId().toString()))
                    .toList();
            
            return UserResource.builder()
                    .userId(suser.getUserId().toString())
                    .username(suser.getUsername())
                    .loginId(suser.getLoginId().toString())
                    .adminFlg(suser.getAdminFlg().booleanValue())
                    .roles(roles)
                    .build();
            //@formatter:on
        };
    }
}
