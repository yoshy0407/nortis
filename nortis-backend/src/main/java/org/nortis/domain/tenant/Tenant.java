package org.nortis.domain.tenant;

import java.time.LocalDateTime;
import java.util.UUID;

import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.validation.Validations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * テナント
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "M_TENANT")
@Entity
public class Tenant {

	/**
	 * テナントID
	 */
	@Id
	@Column(name = "UUID")
	private UUID uuid;
		
	/**
	 * テナント省略名
	 */
	@Column(name = "TENANT_ID", unique = true)
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
		incrementVersion();
	}
	
	/**
	 * テナントIDを変更します
	 * @param tenantId テナントID
	 * @param updateId 更新者ID
	 */
	public void changeTenantId(TenantId tenantId, String updateId) {
		setTenantId(tenantId);
		setUpdateId(updateId);
		setUpdateDt(LocalDateTime.now());
		incrementVersion();
	}
	
	/**
	 * UUIDを設定します
	 * @param uuid UUID
	 */
	public void setUUID(UUID uuid) {
		Validations.notNull(uuid, "テナントID");
		this.uuid = uuid;
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
	
	private void incrementVersion() {
		setVersion(getVersion() + 1L);
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
		entity.setUUID(UUID.randomUUID());
		entity.setTenantId(tenantId);
		entity.setTenantName(tenantName);
		entity.setCreateId(createId);
		entity.setCreateDt(LocalDateTime.now());
		entity.setVersion(1L);
		return entity;
	}
		
}
