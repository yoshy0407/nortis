package org.nortis.domain.user;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.value.AdminFlg;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;

/**
 * ユーザの権限をチェックするドメインサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class AuthorityCheckDomainService {

	private final SuserRepository suserRepository;
	
	/**
	 * 管理者ユーザであるか確認します
	 * @param userId ユーザID
	 * @throws DomainException ロジックエラー
	 */
	public void checkAdminOf(UserId userId) throws DomainException {
		Optional<Suser> optUser = this.suserRepository.get(userId);
		if (optUser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}
		if (!optUser.get().getAdminFlg().equals(AdminFlg.ADMIN)) {
			throw new DomainException(MessageCodes.nortis50005());
		}
	}
	
	/**
	 * 対象のテナントへのアクセス権限があるかどうかチェックします
	 * @param user ユーザ
	 * @param tenant テナント
	 * @throws DomainException ロジックエラー
	 */
	public void checkTenantAuthority(NortisUserDetails user, Tenant tenant) throws DomainException {
		//テナントのAPIキーによる認証の場合
		if (user.isTenant()) {
			if (tenant.getTenantId().equals(TenantId.create(user.getTenantId()[0]))) {
				return;
			}
		//ユーザがある場合
		} else {
			Optional<Suser> optUser = this.suserRepository.get(UserId.create(user.getUsername()));
			if (optUser.isEmpty()) {
				throw new DomainException(MessageCodes.nortis00003("ユーザ"));
			}
			Suser suser = optUser.get();
			
			if (suser.getAdminFlg().equals(AdminFlg.ADMIN)) {
				return;
			}
			
			Optional<TenantUser> optTenantUser = suser.getTenantUserList().stream()
				.filter(tenantUser -> tenantUser.getTenantId().equals(tenant.getTenantId()))
				.findFirst();
			
			if (optTenantUser.isPresent()) {
				return;
			}			
		}
		throw new DomainException(MessageCodes.nortis50005());			
	}
}
