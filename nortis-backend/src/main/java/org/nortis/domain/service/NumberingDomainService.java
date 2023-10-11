package org.nortis.domain.service;

import lombok.AllArgsConstructor;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.annotation.DomainService;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.builder.SelectBuilder;

/**
 * 採番に関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class NumberingDomainService {

    private static final String USER_ID_SEQ = "USER_SEQ";

    private static final String TENANT_ID_SEQ = "TENANT_SEQ";

    private static final String ROLE_ID_SEQ = "TENANT_ROLE_SEQ";

    private static final String ENDPOINT_ID_SEQ = "ENDPOINT_SEQ";

    private static final String CONSUMER_ID_SEQ = "CONSUMER_SEQ";

    private final Config config;

    /**
     * 新しいユーザIDを取得します
     * 
     * @return ユーザID
     */
    public UserId createNewUserId() {
        Long number = getNextVal(USER_ID_SEQ);
        return UserId.createNew(number);
    }

    /**
     * 新しいテナントIDを取得します
     * 
     * @return テナントID
     */
    public TenantId createNewTenantId() {
        Long number = getNextVal(TENANT_ID_SEQ);
        return TenantId.createNew(number);
    }

    /**
     * 新しいロールIDを取得します
     * 
     * @return ロールID
     */
    public RoleId createNewRoleId() {
        Long number = getNextVal(ROLE_ID_SEQ);
        return RoleId.createNew(number);
    }

    /**
     * 新しいエンドポイントIDを取得します
     * 
     * @return エンドポイントID
     */
    public EndpointId createNewEndpointId() {
        Long number = getNextVal(ENDPOINT_ID_SEQ);
        return EndpointId.createNew(number);
    }

    /**
     * 新しいコンシューマIDを取得します
     * 
     * @return コンシューマID
     */
    public ConsumerId createNewConsumerId() {
        Long number = getNextVal(CONSUMER_ID_SEQ);
        return ConsumerId.createNew(number);
    }

    private Long getNextVal(String sequenceName) {
        Sql<?> sql = this.config.getDialect().getSequenceNextValSql(sequenceName, 0);
        //@formatter:off
        return SelectBuilder.newInstance(config)
            .sql(sql.getFormattedSql())
            .getScalarSingleResult(Long.class);
        //@formatter:on
    }

}
