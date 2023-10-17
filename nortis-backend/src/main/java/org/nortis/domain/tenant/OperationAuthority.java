package org.nortis.domain.tenant;

import java.util.stream.Stream;
import lombok.Getter;
import org.nortis.domain.tenant.value.OperationId;

/**
 * テナントのオペレーションに関する列挙型です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
public enum OperationAuthority {
    /**
     * エンドポイント読み取り権限
     */
    READ_ENDPOINT("00001", "エンドポイント読み取り権限", OperationId.READ_ENDPOINT),
    /**
     * エンドポイント読み取り書き込み権限
     */
    READWRITE_ENDPOINT("00002", "エンドポイント書き込み書き込み権限", OperationId.READ_ENDPOINT, OperationId.WRITE_ENDPOINT),
    /**
     * APIキー作成権限
     */
    WRITE_APIKEY("00003", "APIキー作成権限", OperationId.WRITE_APIKEY),
    /**
     * テナント名更新権限
     */
    WRITE_TENANT_NAME("00004", "テナント名更新権限", OperationId.WRITE_TENANT_NAME),
    /**
     * ロール読み取り権限
     */
    READ_TENANT_ROLE("00005", "ロール読み取り権限", OperationId.READ_TENANT_ROLE),
    /**
     * ロール読み取り権限
     */
    READWRITE_TENANT_ROLE("00006", "ロール読み取り権限", OperationId.READ_TENANT_ROLE, OperationId.WRITE_TENANT_ROLE),
    /**
     * テナントの管理者権限
     */
    TENANT_MANAGEMENT_ROLE("00007", "管理者権限", OperationId.values()),
    /**
     * テナントの読み取り専用権限
     */
    TENANT_READ_ROLE("00008", "テナント読み取り専用権限", OperationId.READ_ENDPOINT, OperationId.READ_TENANT_ROLE),
    /**
     * コンシューマ読み取り権限
     */
    READ_CONSUMER_ROLE("00009", "コンシューマ読み取り権限", OperationId.READ_CONSUMER),
    /**
     * コンシューマ更新権限
     */
    READWRITE_CONSUMER_ROLE("00009", "コンシューマ更新権限", OperationId.READ_CONSUMER, OperationId.WRITE_CONSUMER);

    private final String authorityId;

    private final String displayName;

    private final OperationId[] operationIds;

    private OperationAuthority(String authorityId, String displayName, OperationId... operationIds) {
        this.authorityId = authorityId;
        this.displayName = displayName;
        this.operationIds = operationIds;
    }

    /**
     * 権限IDから{@link OperationAuthority}を取得します
     * 
     * @param authorityId 権限ID
     * @return {@link OperationAuthority}
     */
    public static OperationAuthority resolveAuthorityId(String authorityId) {
        //@formatter:off
        return Stream.of(OperationAuthority.values())
                .filter(a -> a.getAuthorityId().equals(authorityId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("存在しないauthorityIdです"));
        //@formatter:on
    }
}
