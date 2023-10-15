package org.nortis.consumer.model;

import org.nortis.consumer.ApiMessageCode;

/**
 * コンシューマのタイプを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class ConsumerType {

    /**
     * 桁数
     */
    private static final int LENGTH = 20;

    private final String code;

    private final String displayName;

    /**
     * インスタンスを生成します
     * 
     * @param code        コード値
     * @param displayName 画面の表示名
     */
    public ConsumerType(String code, String displayName) {
        if (code.length() > LENGTH) {
            // :TODO このエラーでいいかどうか
            throw new IllegalArgumentException(ApiMessageCode.NORTISAPI100001.getMessage(LENGTH));
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
