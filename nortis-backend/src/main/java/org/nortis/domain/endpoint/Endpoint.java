package org.nortis.domain.endpoint;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.SendMessage;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.ApplicationContextAccessor;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.template.TemplateRender;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.data.domain.AbstractAggregateRoot;


/**
 * エンドポイント
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "ENDPOINT")
@Entity(listener = EndpointEntityListener.class)
public class Endpoint extends AbstractAggregateRoot<Endpoint> {

	/**
	 * テナントID
	 */
	@Id
	@Column(name = "TENANT_ID")
	private TenantId tenantId;

	/**
	 * エンドポイントID
	 */
	@Id
	@Column(name = "ENDPOINT_ID")
	private EndpointId endpointId;
	
	/**
	 * エンドポイント名
	 */
	@Column(name = "ENDPOINT_NAME")
	private String endpointName;
	
	/**
	 * サブジェクトテンプレート
	 */
	@Column(name = "SUBJECT_TEMPLATE")
	private String subjectTemplate;
	
	/**
	 * メッセージテンプレート
	 */
	@Column(name = "MESSAGE_TEMPLATE")
	private String messageTemplate;
	
	/**
	 * 作成者ID
	 */
	@Column(name = "CREATE_ID")
	private String createId;
	
	/**
	 * 作成日付
	 */
	@Column(name = "CREATE_DT")
	private LocalDateTime createDt;

	/**
	 * 更新者ID
	 */
	@Setter
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	/**
	 * 更新日付
	 */
	@Setter
	@Column(name = "UPDATE_DT")
	private LocalDateTime updateDt;
	
	/**
	 * バージョン
	 */
	@Version
	@Column(name = "VERSION")
	private long version;

	/**
	 * エンドポイント名を変更します
	 * @param endpointName エンドポイント名
	 * @param updateId 更新者ID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void changeEndpointName(String endpointName, String updateId) throws DomainException {
		setEndpointName(endpointName);
		setUpdateId(updateId);
		setUpdateDt(LocalDateTime.now());
	}
	
	/**
	 * サブジェクトテンプレート名を変更します
	 * @param subjectTemplate サブジェクトテンプレート名
	 * @param updateId 更新者ID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void changeSubjectTemplate(String subjectTemplate, String updateId) throws DomainException {
		setSubjectTemplate(subjectTemplate);
		setUpdateId(updateId);
		setUpdateDt(LocalDateTime.now());
	}

	/**
	 * メッセージテンプレート名を変更します
	 * @param messageTemplate メッセージテンプレート名
	 * @param updateId 更新者ID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void changeMessageTemplate(String messageTemplate, String updateId) throws DomainException {
		setMessageTemplate(messageTemplate);
		setUpdateId(updateId);
		setUpdateDt(LocalDateTime.now());
	}
	
	/**
	 * 送信メッセージを構築します
	 * @param parameter メッセージの引数
	 * @return 送信メッセージ
	 */
	public SendMessage renderMessage(Map<String, Object> parameter) {
		TemplateRender templateRender = ApplicationContextAccessor.getTemplateRender();
		String subject = templateRender.render(this.endpointName, subjectTemplate, parameter);
		String message = templateRender.render(this.endpointId.toString(), messageTemplate, parameter);
		return new SendMessage(subject, message);
	}
	
	/**
	 * 削除をマークします
	 * @param userId ユーザID
	 */
	public void deleted(String userId) {
		setUpdateId(userId);
		setUpdateDt(LocalDateTime.now());
	}
	
	/**
	 * エンドポイントID
	 * @param endpointId エンドポイントID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setEndpointId(EndpointId endpointId) throws DomainException {
		Validations.notNull(endpointId, "エンドポイントID");
		this.endpointId = endpointId;
	}

	/**
	 * テナントID
	 * @param tenantId テナントID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setTenantId(TenantId tenantId) throws DomainException {
		Validations.notNull(tenantId, "テナントID");
		this.tenantId = tenantId;
	}

	/**
	 * エンドポイント名
	 * @param endpointName エンドポイント名
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setEndpointName(String endpointName) throws DomainException {
		Validations.hasText(endpointName, "エンドポイント名");
		Validations.maxTextLength(endpointName, 50, "エンドポイント名");
		this.endpointName = endpointName;
	}
	
	/**
	 * サブジェクトテンプレート
	 * @param subjectTemplate サブジェクトテンプレート
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setSubjectTemplate(String subjectTemplate) throws DomainException {
		Validations.hasText(subjectTemplate, "サブジェクトテンプレート");
		Validations.maxTextLength(subjectTemplate, 100, "サブジェクトテンプレート");
		this.subjectTemplate = subjectTemplate;
	}
	
	/**
	 * メッセージテンプレート
	 * @param messageTemplate メッセージテンプレート
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setMessageTemplate(String messageTemplate) throws DomainException {
		Validations.hasText(messageTemplate, "メッセージテンプレート");
		this.messageTemplate = messageTemplate;
	}

	/**
	 * 作成者IDを設定します
	 * @param createId 作成者ID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setCreateId(String createId) throws DomainException {
		Validations.hasText(createId, "作成者ID");
		this.createId = createId;
	}

	/**
	 * 作成日付を設定します
	 * @param createDt 作成日付
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setCreateDt(LocalDateTime createDt) throws DomainException {
		Validations.notNull(createDt, "作成日付");
		this.createDt = createDt;
	}

	/**
	 * バージョンを設定します
	 * @param version バージョン
	 */
	public void setVersion(long version) {
		this.version = version;
	}
	
	/**
	 * エンドポイントを新規作成します
	 * @param endpointId エンドポイントID
	 * @param tenantId テナントID
	 * @param endpointName エンドポイント名
	 * @param subjectTemplate サブジェクトテンプレート
	 * @param messageTemplate メッセージテンプレート
	 * @param createId 作成者ID
	 * @return 作成したエンドポイント
	 * @throws DomainException ドメインロジックエラー
	 */
	public static Endpoint create(
			EndpointId endpointId, 
			TenantId tenantId, 
			String endpointName, 
			String subjectTemplate,
			String messageTemplate,
			String createId) throws DomainException {
		Endpoint entity = new Endpoint();
		entity.setEndpointId(endpointId);
		entity.setTenantId(tenantId);
		entity.setEndpointName(endpointName);
		entity.setSubjectTemplate(subjectTemplate);
		entity.setMessageTemplate(messageTemplate);
		entity.setCreateId(createId);
		entity.setCreateDt(LocalDateTime.now());
		entity.setVersion(1L);
		return entity;
	}
	
}
