package org.nortis.domain.user;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.Paging;

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
     * 指定されたロールIDのロールを持つユーザを取得します
     * 
     * @param roleId ロールID
     * @return 取得結果
     */
    List<Suser> getFromRoleId(RoleId roleId);

    /**
     * ユーザをリストで取得します
     * 
     * @param paging ページング
     * @return 結果のリスト
     */
    List<Suser> getListPaging(Paging paging);

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
