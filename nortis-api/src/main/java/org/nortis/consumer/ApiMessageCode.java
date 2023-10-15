package org.nortis.consumer;

import java.text.MessageFormat;

/**
 * メッセージコード
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public enum ApiMessageCode {
    /**
     * NORTIS-API10001
     */
    NORTISAPI100001("NORTIS-API10001", "コンシューマタイプのコード値は{0}文字以内で設定する必要があります");

    private final String code;

    private final MessageFormat messageFormat;

    private ApiMessageCode(String code, String messageFormat) {
        this.code = code;
        this.messageFormat = new MessageFormat("[" + this.code + "] " + messageFormat);
    }

    /**
     * メッセージコードを返します
     * 
     * @return メッセージコード
     */
    public String getCode() {
        return code;
    }

    /**
     * メッセージフォーマットを返します
     * 
     * @return メッセージフォーマット
     */
    public MessageFormat getMessageFormat() {
        return messageFormat;
    }

    /**
     * メッセージを返却します
     * 
     * @param args 引数
     * @return メッセージ
     */
    public String getMessage(Object... args) {
        return this.messageFormat.format(args);
    }
}
