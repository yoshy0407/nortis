package org.nortis.domain.tenant;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.data.domain.AbstractAggregateRoot;

/**
 * テナント
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "TENANT")
@Entity(listener = TenantEntityListener.class)
public class Tenant extends AbstractAggregateRoot<Tenant> {

	/**
	 * テナント省略名
	 */
	@Id
	@Column(name = "TENANT_ID")
	private TenantId tenantId;
	
	/**
	 * テナント名
	 */
	@Column(name = "TENANT_NAME")
	private String tenantName;
	
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
	@Setter
	@Version
	@Column(name = "VERSION")
	private long version;

	/**
	 * テナント名を変更します
	 * @param tenantName テナント名
	 * @param updateId 更新者ID
	 */
	public void changeTenantName(String tenantName, String updateId) {
		setTenantName(tenantName);
		setUpdateId(updateId);
		setUpdateDt(LocalDateTime.now());
	}	
	
	/**
	 * エンドポイントを作成します
	 * @param endpointId エンドポイント
	 * @param endpointName エンドポイント名
	 * @param subjectTemplate サブジェクトテンプレート
	 * @param templateText メッセージテンプレート
	 * @param createId 作成者ID
	 * @return エンドポイント
	 */
	public Endpoint createEndpoint(EndpointId endpointId, String endpointName, 
			String subjectTemplate, String templateText, String createId) {
		return Endpoint.create(endpointId, this.tenantId, endpointName, subjectTemplate, templateText, createId);
	}
	
	/**
	 * APIキーを作成します
	 * @return 認証
	 */
	public Authentication createApiKey() {
		return Authentication.createFromTenant(this.tenantId);
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
	 * テナント名を設定します
	 * @param tenantName テナント名
	 */
	public void setTenantName(String tenantName) {
		Validations.hasText(tenantName, "テナント名");
		Validations.maxTextLength(tenantName, 50, "テナント名");
		this.tenantName = tenantName;
	}

	/**
	 * テナントIDを設定します
	 * @param tenantId テナントID
	 */
	public void setTenantId(TenantId tenantId) {
		Validations.notNull(tenantId, "テナント省略名");
		this.tenantId = tenantId;
	}

	/**
	 * 作成者IDを設定します
	 * @param createId 作成者ID
	 */
	public void setCreateId(String createId) {
		Validations.hasText(createId, "作成者ID");
		this.createId = createId;
	}
	
	/**
	 * 作成日付を設定します
	 * @param createDt 作成日付
	 */
	public void setCreateDt(LocalDateTime createDt) {
		Validations.notNull(createDt, "作成者ID");
		this.createDt = createDt;		
	}
	
	/**
	 * エンティティを新規作成します
	 * @param tenantId テナントID
	 * @param tenantName テナント名
	 * @param createId 作成者ID
	 * @return テナント
	 */
	public static Tenant create(TenantId tenantId, String tenantName, String createId) {
		final Tenant entity = new Tenant();
		entity.setTenantId(tenantId);
		entity.setTenantName(tenantName);
		entity.setCreateId(createId);
		entity.setCreateDt(LocalDateTime.now());
		entity.setVersion(1L);
		return entity;
	}
		
}
