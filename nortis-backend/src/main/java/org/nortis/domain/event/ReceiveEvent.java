package org.nortis.domain.event;

import java.time.LocalDateTime;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.value.EventId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 受信イベント
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
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
	public void subscribed() {
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
	
	public void setEndpointId(EndpointId endpointId) {
		Validations.notNull(endpointId, "エンドポイントID");
		this.endpointId = endpointId;
	}
	
	public void setOccuredOn(LocalDateTime occuredOn) {
		Validations.notNull(occuredOn, "発生時刻");
		this.occuredOn = occuredOn;
	}
	
	public void setSubject(String subject) {
		Validations.maxTextLength(subject, 100, "件名");
		this.subject = subject;
	}
	
	public void setMessageBody(String messageBody) {
		Validations.maxTextLength(messageBody, 1000, "メッセージ本体");
		this.messageBody = messageBody;
	}
	
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
