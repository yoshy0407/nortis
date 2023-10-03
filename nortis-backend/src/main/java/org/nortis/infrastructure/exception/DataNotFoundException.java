package org.nortis.infrastructure.exception;

import org.nortis.infrastructure.message.MessageCode;

/**
 * データが存在しない場合の例外です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DataNotFoundException extends DomainException {

    private static final long serialVersionUID = 1L;

    /**
     * 例外を構築します
     * 
     * @param messageCode メッセージコード
     * @param cause       例外
     */
    public DataNotFoundException(MessageCode messageCode, Throwable cause) {
        super(messageCode, cause);
    }

    /**
     * 例外を構築します
     * 
     * @param messageCode メッセージコード
     */
    public DataNotFoundException(MessageCode messageCode) {
        super(messageCode);
    }

}
