package org.nortis.domain.endpoint.value;

import lombok.Getter;
import org.nortis.consumer.model.MessageTextType;
import org.seasar.doma.Domain;

/**
 * テキストタイプ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Domain(valueType = String.class, accessorMethod = "getValue", factoryMethod = "resolve")
public enum TextType {
    /**
     * テキスト
     */
    TEXT("00001", "TEXT", MessageTextType.TEXT),
    /**
     * HTML
     */
    HTML("00002", "HTML", MessageTextType.HTML);

    private final String textType;

    private final String textTypeName;

    private final MessageTextType messageTextType;

    private TextType(String textType, String textTypeName, MessageTextType messageTextType) {
        this.textType = textType;
        this.textTypeName = textTypeName;
        this.messageTextType = messageTextType;
    }

    /**
     * 値を取得します
     * 
     * @return テキストタイプの値
     */
    public String getValue() {
        return this.textType;
    }

    /**
     * 文字列から値を解決します
     * 
     * @param value 文字列
     * @return 値
     */
    public static TextType resolve(String value) {
        for (TextType textType : TextType.values()) {
            if (textType.getValue().equals(value)) {
                return textType;
            }
        }
        throw new IllegalStateException("指定された値が存在しません。　値：" + value);
    }
}
