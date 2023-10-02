package org.nortis.domain.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.endpoint.RenderedMessage;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.event.value.EventId;
import org.nortis.domain.event.value.Subscribed;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.doma.NortisEntityListener;
import org.nortis.infrastructure.doma.entity.RootEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * 受信イベント
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "RECEIVE_EVENT")
@Entity(listener = NortisEntityListener.class, metamodel = @Metamodel)
public class ReceiveEvent extends RootEntity {

    /**
     * イベントID
     */
    @Id
    @Column(name = "EVENT_ID")
    private EventId eventId;

    /**
     * テナントID
     */
    @Column(name = "TENANT_ID")
    private TenantId tenantId;

    /**
     * エンドポイントID
     */
    @Column(name = "ENDPOINT_ID")
    private EndpointId endpointId;

    /**
     * 発生時刻
     */
    @Column(name = "OCCURED_ON")
    private LocalDateTime occuredOn;

    /**
     * JSONで保管したパラメータ
     */
    @Setter
    @Column(name = "PARAMETER_JSON")
    private String parameterJson;

    /**
     * 受信済みフラグ
     */
    @Setter
    @Column(name = "SUBSCRIBED")
    private Subscribed subscribed;

    @Transient
    private List<SendMessage> sendMessageList = new ArrayList<>();

    /**
     * メッセージを取得します
     * 
     * @param textType テキストタイプ
     * @return 取得結果
     */
    public Optional<SendMessage> getMessage(TextType textType) {
        //@formatter:off
        return this.sendMessageList.stream()
                .filter(d -> d.getTextType().equals(textType))
                .findFirst();
        //@formatter:on
    }

    /**
     * 受信済みに設定します
     */
    public void subscribe() {
        setSubscribed(Subscribed.TRUE);
        setUpdateDt(LocalDateTime.now());
    }

    /**
     * イベントIDを設定します
     * 
     * @param eventId イベントID
     * @throws DomainException ドメインロジックエラー
     */
    public void setEventId(EventId eventId) throws DomainException {
        Validations.notNull(eventId, "イベントID");
        this.eventId = eventId;
    }

    /**
     * テナントIDを設定します
     * 
     * @param tenantId テナントID
     * @throws DomainException ドメインロジックエラー
     */
    public void setTenantId(TenantId tenantId) throws DomainException {
        Validations.notNull(tenantId, "テナントID");
        this.tenantId = tenantId;
    }

    /**
     * エンドポイントIDを設定します
     * 
     * @param endpointId エンドポイントID
     * @throws DomainException ドメインロジックエラー
     */
    public void setEndpointId(EndpointId endpointId) throws DomainException {
        Validations.notNull(endpointId, "エンドポイントID");
        this.endpointId = endpointId;
    }

    /**
     * 発生時刻を設定します
     * 
     * @param occuredOn 発生時刻
     * @throws DomainException ドメインロジックエラー
     */
    public void setOccuredOn(LocalDateTime occuredOn) throws DomainException {
        Validations.notNull(occuredOn, "発生時刻");
        this.occuredOn = occuredOn;
    }

    /**
     * 受信イベントを新規作成します
     * 
     * @param tenantId        テナントID
     * @param endpointId      エンドポイントID
     * @param sendMessageList 送信メッセージ
     * @param parameterJson   パラメータのJSON文字列
     * @return 受信イベント
     * @throws DomainException ドメインロジックエラー
     */
    public static ReceiveEvent create(TenantId tenantId, EndpointId endpointId, String parameterJson,
            List<RenderedMessage> sendMessageList) throws DomainException {
        ReceiveEvent receiveEvent = new ReceiveEvent();
        receiveEvent.setEventId(EventId.createNew());
        receiveEvent.setTenantId(tenantId);
        receiveEvent.setEndpointId(endpointId);
        receiveEvent.setOccuredOn(LocalDateTime.now());
        receiveEvent.setSubscribed(Subscribed.FALSE);
        receiveEvent.setParameterJson(parameterJson);
        sendMessageList.forEach(m -> receiveEvent.getSendMessageList()
                .add(SendMessage.createFromMessage(receiveEvent.getEventId(), m)));
        return receiveEvent;
    }

}
