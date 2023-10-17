package org.nortis.port.rest;

import lombok.AllArgsConstructor;
import org.nortis.application.authentication.UserLoginService;
import org.nortis.domain.authentication.Authentication;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.port.rest.payload.LoginRequest;
import org.nortis.port.rest.payload.LogoutRequest;
import org.nortis.port.rest.resource.ApiKeyResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * ログインに関するコントローラ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RestController
public class UserLoginRestController {

    private final UserLoginService userLoginService;

    /**
     * ログイン処理
     * 
     * @param request リクエスト
     * @return 認証結果のAPIキー
     * @throws DomainException ロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user/login")
    public ApiKeyResource login(@RequestBody LoginRequest request) throws DomainException {
        return this.userLoginService.login(request.loginId(), request.password(), translator());
    }

    /**
     * ログアウト処理
     * 
     * @param request リクエスト
     * @throws DomainException ロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/user/logout")
    public void logout(@RequestBody LogoutRequest request) throws DomainException {
        this.userLoginService.logout(request.loginId());
    }

    private ApplicationTranslator<Authentication, ApiKeyResource> translator() {
        return authentication -> new ApiKeyResource(authentication.getApiKey().toString());
    }

}
