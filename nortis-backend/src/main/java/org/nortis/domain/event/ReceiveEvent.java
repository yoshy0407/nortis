package org.nortis.domain.event;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.value.EventId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 * 受信イベント
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "RECEIVE_EVENT")
@Entity
public class ReceiveEvent {

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
	 * 受信済みフラグ
	 */
	@Setter
	@Column(name = "SUBSCRIBED")
	private boolean subscribed;
	
	/**
	 * 件名
	 */
	@Column(name = "SUBJECT")
	private String subject;
	
	/**
	 * メッセージ本体
	 */
	@Column(name = "MESSAGE_BODY")
	private String messageBody;
	
	/**
	 * 更新日付
	 */
	@Setter
	@Column(name = "UPDATE_DT")
	private LocalDateTime updateDt;
	
	/**
	 * 受信済みに設定します
	 */
	public void subscribe() {
		setSubscribed(true);
		setUpdateDt(LocalDateTime.now());
	}
	
	/**
	 * イベントIDを設定します
	 * @param eventId イベントID
	 */
	public void setEventId(EventId eventId) {
		Validations.notNull(eventId, "イベントID");
		this.eventId = eventId;
	}

	/**
	 * テナントIDを設定します
	 * @param tenantId テナントID
	 */
	public void setTenantId(TenantId tenantId) {
		Validations.notNull(tenantId, "テナントID");
		this.tenantId = tenantId;
	}
	
	/**
	 * エンドポイントIDを設定します
	 * @param endpointId エンドポイントID
	 */
	public void setEndpointId(EndpointId endpointId) {
		Validations.notNull(endpointId, "エンドポイントID");
		this.endpointId = endpointId;
	}
	
	/**
	 * 発生時刻を設定します
	 * @param occuredOn 発生時刻
	 */
	public void setOccuredOn(LocalDateTime occuredOn) {
		Validations.notNull(occuredOn, "発生時刻");
		this.occuredOn = occuredOn;
	}
	
	/**
	 * 件名を設定します
	 * @param subject 件名
	 */
	public void setSubject(String subject) {
		Validations.maxTextLength(subject, 100, "件名");
		this.subject = subject;
	}
	
	/**
	 * メッセージ本部を設定します
	 * @param messageBody メッセージ本文
	 */
	public void setMessageBody(String messageBody) {
		Validations.maxTextLength(messageBody, 1000, "メッセージ本体");
		this.messageBody = messageBody;
	}
	
	/**
	 * 受信イベントを新規作成します
	 * @param tenantId テナントID
	 * @param endpointId エンドポイントID
	 * @param subject 件名
	 * @param messageBody メッセージ本文
	 * @return 受信イベント
	 */
	public static ReceiveEvent create(
			TenantId tenantId,
			EndpointId endpointId,
			String subject,
			String messageBody) {
		ReceiveEvent receiveEvent = new ReceiveEvent();
		receiveEvent.setEventId(EventId.createNew());
		receiveEvent.setTenantId(tenantId);
		receiveEvent.setEndpointId(endpointId);
		receiveEvent.setOccuredOn(LocalDateTime.now());
		receiveEvent.setSubscribed(false);
		receiveEvent.setSubject(subject);
		receiveEvent.setMessageBody(messageBody);
		return receiveEvent;
	}
	
}
