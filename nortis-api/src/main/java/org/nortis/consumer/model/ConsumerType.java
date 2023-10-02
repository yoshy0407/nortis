package org.nortis.consumer.model;

/**
 * コンシューマのタイプを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ConsumerType {

    private final String code;

    private final String displayName;

    /**
     * インスタンスを生成します
     * 
     * @param code        コード値
     * @param displayName 画面の表示名
     */
    public ConsumerType(String code, String displayName) {
        if (code.length() > 20) {
            throw new IllegalArgumentException("コンシューマタイプのコード値は20文字以内で設定する必要があります");
        }
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * コード値を取得します
     * 
     * @return コード値
     */
    public String getCode() {
        return code;
    }

    /**
     * 画面の表示名
     * 
     * @return 画面の表示名
     */
    public String getDisplayName() {
        return displayName;
    }

}
