package org.nortis.domain.service.password;

import lombok.AllArgsConstructor;
import org.nortis.infrastructure.message.MessageCode;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * パスワードの長さをチェックするポリシーオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class PasswordLengthPolicy implements PasswordPolicy {

    private final int checkLength;

    @Override
    public boolean isSatisfied(String rawPassword) {
        return rawPassword.length() >= checkLength;
    }

    @Override
    public MessageCode getMessageCode() {
        return MessageCodes.nortis70001(checkLength);
    }

}
