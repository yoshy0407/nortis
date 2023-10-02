package org.nortis.domain.tenant.value;

import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * テナントのオペレーションを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Domain(valueType = String.class, accessorMethod = "getValue", factoryMethod = "resolve")
@Getter
public enum OperationId {
    /**
     * エンドポイント読み取り権限
     */
    READ_ENDPOINT("00101", "エンドポイント読み取り権限"),
    /**
     * エンドポイント読み取り書き込み権限
     */
    WRITE_ENDPOINT("00102", "エンドポイント書き込み権限"),
    /**
     * APIキー作成権限
     */
    WRITE_APIKEY("00201", "APIキー作成権限"),
    /**
     * テナント名更新権限
     */
    WRITE_TENANT_NAME("00301", "テナント名更新権限"),
    /**
     * ロール読み取り権限
     */
    READ_TENANT_ROLE("00401", "ロール読み取り権限"),
    /**
     * ロール書き込み権限
     */
    WRITE_TENANT_ROLE("00401", "ロール書き込み権限"),
    /**
     * コンシューマ読み取り権限
     */
    READ_CONSUMER("00501", "コンシューマ読み取り権限"),
    /**
     * コンシューマ書き込み権限
     */
    WRITE_CONSUMER("00502", "コンシューマ書き込み権限");

    private final String value;

    private final String displayName;

    private OperationId(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     * 値に対応するオペレーションIDを取得します
     * 
     * @param value 値
     * @return ログインフラグ
     */
    public static OperationId resolve(String value) {
        for (OperationId operationId : OperationId.values()) {
            if (operationId.getValue().equals(value)) {
                return operationId;
            }
        }
        throw new IllegalStateException("指定された値が存在しません。　値：" + value);
    }

}
