package org.nortis.application.authentication;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.service.PasswordDomainService;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.domain.user.value.LoginId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.transaction.annotation.Transactional;

/**
 * ユーザのログインに関するサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
@ApplicationService
public class UserLoginService {

    private final SuserRepository suserRepository;

    private final AuthenticationRepository authenticationRepository;

    private final PasswordDomainService passwordDomainService;

    /**
     * ログインを実施します
     * 
     * @param <R>        変換後のクラス
     * 
     * @param loginId    ログインID
     * @param password   パスワード
     * @param translator 変換処理
     * @return 認証
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R login(String loginId, String password, ApplicationTranslator<Authentication, R> translator)
            throws DomainException {

        Optional<Suser> optSuser = this.suserRepository.getByLoginId(LoginId.create(loginId));
        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis50004());
        }
        Suser suser = optSuser.get();

        if (!checkPassword(password, suser.getHashedPassword())) {
            throw new DomainException(MessageCodes.nortis50004());
        }
        Authentication authentication = suser.login();

        this.suserRepository.update(suser);
        this.authenticationRepository.save(authentication);
        return translator.translate(authentication);
    }

    private boolean checkPassword(String password, HashedPassword hashedPassword) {
        return this.passwordDomainService.match(password, hashedPassword);
    }

    /**
     * ログアウト処理を実施します
     * 
     * @param loginId ログインID
     * @throws DomainException ドメインロジックエラー
     */
    public void logout(String loginId) throws DomainException {
        Optional<Suser> optSuser = this.suserRepository.getByLoginId(LoginId.create(loginId));
        if (optSuser.isEmpty()) {
            throw new DomainException(MessageCodes.nortis00003("ユーザ"));
        }

        Suser login = optSuser.get();
        login.logout();
        this.suserRepository.update(login);

        Optional<Authentication> optAuth = this.authenticationRepository.getFromUserId(login.getUserId());
        // 認証がなければ、正しい状態なのでそのままに
        optAuth.ifPresent(auth -> {
            this.authenticationRepository.remove(auth);
        });
    }
}
