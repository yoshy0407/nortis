package org.nortis.domain.service.password;

import org.nortis.infrastructure.message.MessageCode;

/**
 * パスワードポリシーを表すインタフェースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface PasswordPolicy {

    /**
     * ポリシーを満たすかどうかチェックします
     * 
     * @param rawPassword パスワード
     * @return チェック結果
     */
    boolean isSatisfied(String rawPassword);

    /**
     * エラー時のメッセージコード
     * 
     * @return メッセージコード
     */
    MessageCode getMessageCode();
}
