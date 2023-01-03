package org.nortis.domain.endpoint;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
	 */
	public void changeEndpointName(String endpointName, String updateId) {
		setEndpointName(endpointName);
		setUpdateId(updateId);
		setUpdateDt(LocalDateTime.now());
	}
	
	/**
	 * エンドポイントID
	 * @param endpointId エンドポイントID
	 */
	public void setEndpointId(EndpointId endpointId) {
		Validations.notNull(endpointId, "エンドポイントID");
		this.endpointId = endpointId;
	}

	/**
	 * テナントID
	 * @param tenantId テナントID
	 */
	public void setTenantId(TenantId tenantId) {
		Validations.notNull(tenantId, "テナントID");
		this.tenantId = tenantId;
	}

	/**
	 * エンドポイント名
	 * @param endpointName エンドポイント名
	 */
	public void setEndpointName(String endpointName) {
		Validations.hasText(endpointName, "エンドポイント名");
		Validations.maxTextLength(endpointName, 50, "エンドポイント名");
		this.endpointName = endpointName;
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
	 * @param createId 作成者ID
	 * @return 作成したエンドポイント
	 */
	public static Endpoint create(
			EndpointId endpointId, 
			TenantId tenantId, 
			String endpointName, 
			String createId) {
		Endpoint entity = new Endpoint();
		entity.setEndpointId(endpointId);
		entity.setTenantId(tenantId);
		entity.setEndpointName(endpointName);
		entity.setCreateId(createId);
		entity.setCreateDt(LocalDateTime.now());
		entity.setVersion(1L);
		return entity;
	}
	
}
