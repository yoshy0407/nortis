package org.nortis.domain.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * ユーザに関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * 
 */
@AllArgsConstructor
@DomainService
public class SuserDomainService {

    private SuserRepository suserRepository;

    /**
     * ユーザの登録前にチェックを行います
     * 
     * @param userId  ユーザID
     * @param loginId ログインID
     * @throws DomainException チェックエラー
     */
    public void beforeRegisterCheck(UserId userId, LoginId loginId) throws DomainException {
        Optional<Suser> optUser1 = this.suserRepository.getByUserId(userId);
        if (optUser1.isPresent()) {
            throw new UnexpectedException(MessageCodes.nortis50006(), null);
        }
        Optional<Suser> optUser2 = this.suserRepository.getByLoginId(loginId);
        if (optUser2.isPresent()) {
            throw new DomainException(MessageCodes.nortis50003());
        }

    }

}
