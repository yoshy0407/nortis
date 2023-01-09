package org.nortis.domain.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.event.value.EventId;
import org.nortis.domain.event.value.Subscribed;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.exception.DomainException;
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
	private Subscribed subscribed;
	
	/**
	 * テンプレートパラメーター
	 */
	@Column(name = "TEMPLATE_PARAMETER")
	private String templateParameter;
	
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
		setSubscribed(Subscribed.TRUE);
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
	 * テンプレートパラメーターを設定します
	 * @param templateParameter テンプレートパラメーター
	 */
	public void setTemplateParameter(String templateParameter) {
		try {
			ApplicationContextAccessor.getObjectMapper().readTree(templateParameter);
		} catch (JsonProcessingException e) {
			throw new DomainException("MSG40001", e);
		}
		this.templateParameter = templateParameter;
	}
	
	/**
	 * 受信イベントを新規作成します
	 * @param tenantId テナントID
	 * @param endpointId エンドポイントID
	 * @param parameterJson テンプレートのパラメータのJSON
	 * @return 受信イベント
	 */
	public static ReceiveEvent create(
			TenantId tenantId,
			EndpointId endpointId,
			String parameterJson) {
		ReceiveEvent receiveEvent = new ReceiveEvent();
		receiveEvent.setEventId(EventId.createNew());
		receiveEvent.setTenantId(tenantId);
		receiveEvent.setEndpointId(endpointId);
		receiveEvent.setOccuredOn(LocalDateTime.now());
		receiveEvent.setSubscribed(Subscribed.FALSE);
		receiveEvent.setTemplateParameter(parameterJson);
		return receiveEvent;
	}
	
}
