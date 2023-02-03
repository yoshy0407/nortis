package org.nortis.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * テナントユーザエンティティ
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "TENANT_USER")
public class TenantUser {

	@Id
	@Column(name = "USER_ID")
	private final UserId userId;
	
	@Id
	@Column(name = "TENANT_ID")
	private final TenantId tenantId;
	
	@Setter
	@Transient
	private boolean insert = false;
	
	@Setter
	@Transient
	private boolean deleted = false;

	/**
	 * インスタンスを生成します
	 * @param userId ユーザID
	 * @param tenantId テナントID
	 */
	public TenantUser(UserId userId, TenantId tenantId) {
		this.userId = userId;
		this.tenantId = tenantId;
	}
	
}
