package org.nortis.domain.consumer;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import org.nortis.consumer.ConsumerParameters;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.doma.NortisEntityListener;
import org.nortis.infrastructure.doma.entity.RootEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * コンシューマ定義
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@ToString
@Table(name = "CONSUMER")
@Entity(metamodel = @Metamodel, listener = NortisEntityListener.class)
public class Consumer extends RootEntity {

    @Id
    @Column(name = "TENANT_ID")
    private TenantId tenantId;

    /**
     * コンシューマID
     */
    @Id
    @Column(name = "CONSUMER_ID")
    private ConsumerId consumerId;

    /**
     * コンシューマ名
     */
    @Column(name = "CONSUMER_NAME")
    private String consumerName;

    /**
     * コンシューマタイプのコード値
     */
    @Column(name = "CONSUMER_TYPE_CODE")
    private String consumerTypeCode;

    /**
     * テキストタイプ
     */
    @Column(name = "TEXT_TYPE")
    private TextType textType;

    /**
     * パラメータのリスト
     */
    @Transient
    private final Map<String, ConsumerParameter> parameters = new LinkedHashMap<>();

    /**
     * サブスクライブしているエンドポイント
     */
    @Transient
    private final Map<EndpointId, ConsumerSubscribe> subscribes = new LinkedHashMap<>();

    @SuppressWarnings("javadoc")
    public void setTenantId(TenantId tenantId) throws DomainException {
        Validations.notNull(tenantId, "テナントID");
        this.tenantId = tenantId;
    }

    @SuppressWarnings("javadoc")
    public void setConsumerId(ConsumerId consumerId) throws DomainException {
        Validations.notNull(consumerId, "コンシューマID");
        this.consumerId = consumerId;
    }

    /**
     * コンシューマタイプをセットします
     * 
     * @param consumerTypeCode コンシューマタイプ
     */
    public void setConsumerTypeCode(String consumerTypeCode) {
        this.consumerTypeCode = consumerTypeCode;
    }

    /**
     * set ConsumerName
     * 
     * @param consumerName コンシューマ名
     * @throws DomainException チェックエラー
     */
    public void setConsumerName(String consumerName) throws DomainException {
        Validations.notNull(consumerName, "コンシューマー名");
        Validations.maxTextLength(consumerName, 100, "コンシューマ名");
        this.consumerName = consumerName;
    }

    @SuppressWarnings("javadoc")
    public void setTextType(TextType textType) throws DomainException {
        Validations.notNull(textType, "テキストタイプ");
        this.textType = textType;
    }

    /**
     * コンシューマ名を変更します
     * 
     * @param name コンシューマ名
     * @throws DomainException チェックエラー
     */
    public void changeName(String name) throws DomainException {
        setConsumerName(name);
    }

    /**
     * テキストタイプを変更します
     * 
     * @param textType テキストタイプ
     * @throws DomainException チェックエラー
     */
    public void changeTextType(TextType textType) throws DomainException {
        setTextType(textType);
    }

    /**
     * パラメータを追加します
     * 
     * @param parameterName  パラメータ名
     * @param parameterValue パラメータ値
     * @throws DomainException チェックエラー
     */
    protected void addParameter(String parameterName, String parameterValue) throws DomainException {
        ConsumerParameter parameter = ConsumerParameter.create(consumerId, parameterName, parameterValue);
        parameter.setInsert();
        this.parameters.put(parameter.getParameterName(), parameter);
    }

    /**
     * パラメータを更新します
     * 
     * @param parameter パラメータ
     * @throws DomainException チェックエラー
     */
    public void updateParameter(Map<String, String> parameter) throws DomainException {
        for (Entry<String, String> entry : parameter.entrySet()) {
            ConsumerParameter consumerParameter = this.parameters.get(entry.getKey());
            if (consumerParameter == null) {
                addParameter(entry.getKey(), entry.getValue());
            } else {
                consumerParameter.setParameterValue(entry.getValue());
            }
        }
    }

    /**
     * エンドポイントを購読します
     * 
     * @param endpointId エンドポイントID
     */
    public void subscribe(EndpointId endpointId) {
        ConsumerSubscribe consumerSubscribe = ConsumerSubscribe.create(consumerId, endpointId);
        consumerSubscribe.setInsert();
        this.subscribes.put(consumerSubscribe.getEndpointId(), consumerSubscribe);
    }

    /**
     * エンドポイントを購読終了します
     * 
     * @param endpointId エンドポイントID
     */
    public void unsubscribe(EndpointId endpointId) {
        ConsumerSubscribe consumerSubscribe = this.subscribes.get(endpointId);
        if (consumerSubscribe == null) {
            // なければすでに購読していないので、終了
            return;
        }
        consumerSubscribe.setDelete();
    }

    /**
     * パラメータを作成します
     * 
     * @return パラメータ
     */
    public ConsumerParameters getParameter() {
        //@formatter:off
        LinkedHashMap<String, String> map = this.parameters.values().stream()
                .collect(Collectors.toMap(
                        d -> d.getParameterName(), 
                        d -> d.getParameterValue(), 
                        (d1, d2) -> d1, 
                        LinkedHashMap::new));
        //@formatter:on
        return new ConsumerParameters(map);
    }

    /**
     * 作成します
     * 
     * @param tenantId         テナントID
     * @param consumerId       コンシューマID
     * @param consumerName     コンシューマ名
     * @param consumerTypeCode コンシューマタイプコード
     * @param textType         テキストタイプ
     * @param parameter        パラメータ
     * @return 作成したオブジェクト
     * @throws DomainException ドメインロジックエラー
     */
    public static Consumer create(TenantId tenantId, ConsumerId consumerId, String consumerName,
            String consumerTypeCode, TextType textType, Map<String, String> parameter) throws DomainException {
        Consumer entity = new Consumer();
        entity.setTenantId(tenantId);
        entity.setConsumerId(consumerId);
        entity.setConsumerName(consumerName);
        entity.setConsumerTypeCode(consumerTypeCode);
        entity.setTextType(textType);
        entity.setCreateId(SecurityContextUtils.getCurrentAuthorizedId().toString());
        entity.setCreateDt(LocalDateTime.now());
        entity.setVersion(1L);
        for (Entry<String, String> entry : parameter.entrySet()) {
            entity.addParameter(entry.getKey(), entry.getValue());
        }
        return entity;
    }

}
