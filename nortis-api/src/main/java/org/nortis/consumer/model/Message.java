package org.nortis.consumer.model;

/**
 * 送信メッセージを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class Message {

    private final MessageTextType textType;

    private final String subject;

    private final String messageBody;

    /**
     * インスタンスを生成します
     * 
     * @param textType    テキストタイプ
     * @param subject     サブジェクト
     * @param messageBody メッセージボディ
     */
    public Message(MessageTextType textType, String subject, String messageBody) {
        this.textType = textType;
        this.subject = subject;
        this.messageBody = messageBody;
    }

    /**
     * テキストタイプを返却します
     * 
     * @return テキストタイプ
     */
    public MessageTextType getTextType() {
        return textType;
    }

    /**
     * サブジェクトを返却します
     * 
     * @return サブジェクト
     */
    public String getSubject() {
        return subject;
    }

    /**
     * メッセージボディを返却します
     * 
     * @return メッセージボディ
     */
    public String getMessageBody() {
        return messageBody;
    }

}
