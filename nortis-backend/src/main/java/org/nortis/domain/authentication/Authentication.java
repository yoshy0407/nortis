package org.nortis.domain.authentication;

import java.time.LocalDateTime;
import lombok.Getter;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;


/**
 * 認証の集約です
 * @author yoshiokahiroshi
 * @version
 */
@Getter
@Table(name = "AUTHENTICATION")
@Entity(metamodel = @Metamodel)
public class Authentication {

	@Id
	@Column(name = "API_KEY")
	private ApiKey apiKey;
	
	@Column(name = "TENANT_ID")
	private TenantId tenantId;
	
	@Column(name = "USER_ID")
	private UserId userId;
	
	@Column(name = "LAST_ACCESS_DATETIME")
	private LocalDateTime lastAccessDatetime;
	
	/**
	 * APIキーを設定します
	 * @param apiKey APIキーを設定します
	 * @throws DomainException ドメインロジックエラー
	 */
	public void setApiKey(ApiKey apiKey) throws DomainException {
		Validations.notNull(apiKey, "APIキー");
		this.apiKey = apiKey;
	}

	/**
	 * テナントIDを設定します
	 * @param tenantId テナントID
	 */
	public void setTenantId(TenantId tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * ユーザIDを設定します
	 * @param userId ユーザIDを設定します
	 */
	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	/**
	 * 最終アクセス日時を設定します
	 * @param lastAccessDatetime 最終アクセス日時
	 */
	public void setLastAccessDatetime(LocalDateTime lastAccessDatetime) {
		this.lastAccessDatetime = lastAccessDatetime;
	}
	
	/**
	 * テナント用の認証を作成します
	 * @param tenantId テナントID
	 * @return 認証
	 * @throws DomainException ドメインロジックエラー
	 */
	public static Authentication createFromTenant(TenantId tenantId) throws DomainException {
		Validations.notNull(tenantId, "テナントID");
		ApiKey apiKey = ApiKey.newKey();
		Authentication authentication = new Authentication();
		authentication.setApiKey(apiKey);
		authentication.setTenantId(tenantId);
		return authentication;
	}

	/**
	 * ユーザ用の認証を作成します
	 * @param userId ユーザID
	 * @return 認証
	 * @throws DomainException ドメインロジックエラー
	 */
	public static Authentication createFromUserId(UserId userId) throws DomainException {
		Validations.notNull(userId, "ユーザID");
		ApiKey apiKey = ApiKey.newKey();
		Authentication authentication = new Authentication();
		authentication.setApiKey(apiKey);
		authentication.setUserId(userId);
		return authentication;
	}

}
