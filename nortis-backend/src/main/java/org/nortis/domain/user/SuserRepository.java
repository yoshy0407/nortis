package org.nortis.domain.user;

import java.util.Optional;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;

/**
 * ユーザリポジトリインタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface SuserRepository {

    /**
     * ユーザを取得します
     * 
     * @param userId ユーザID
     * @return ユーザ
     */
    Optional<Suser> getByUserId(UserId userId);

    /**
     * ユーザを取得します
     * 
     * @param loginId ログインID
     * @return ユーザ
     */
    Optional<Suser> getByLoginId(LoginId loginId);

    /**
     * ユーザを保存します
     * 
     * @param suser ユーザ
     */
    void save(Suser suser);

    /**
     * ユーザを更新します
     * 
     * @param suser ユーザ
     */
    void update(Suser suser);

    /**
     * ユーザを削除します
     * 
     * @param suser ユーザ
     */
    void remove(Suser suser);

}
