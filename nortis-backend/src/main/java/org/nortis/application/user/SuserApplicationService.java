package org.nortis.application.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.TenantDomainService;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;

/**
 * ユーザのアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class SuserApplicationService {

	private final SuserRepository suserRepository;
	
	private final AuthenticationDomainService authenticationDomainService;
	
	private final TenantDomainService tenantDomainService;
	
	/**
	 * 管理者ユーザを登録します
	 * @param <R> 結果クラス
	 * @param command ユーザ登録コマンド
	 * @param translator 結果変換クラス
	 * @return 結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R registerAdminUser(SuserRegisterCommand command, ApplicationTranslator<Suser, R> translator) throws DomainException {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isPresent()) {
			throw new DomainException(MessageCodes.nortis50003());
		}
		
		Suser suser = Suser.createAdmin(userId, command.username(), command.password(), command.createUserId());
		this.suserRepository.save(suser);
		return translator.translate(suser);
	}

	/**
	 * 一般ユーザを登録します
	 * @param <R> 結果クラス
	 * @param command ユーザ登録コマンド
	 * @param tenantIds テナントID
	 * @param translator 結果変換クラス
	 * @return 結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R registerMemberUser(
			SuserRegisterCommand command, 
			String[] tenantIds,
			ApplicationTranslator<Suser, R> translator) throws DomainException {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isPresent()) {
			throw new DomainException(MessageCodes.nortis50003());
		}
		
		List<TenantId> tenantIdList = new ArrayList<>();
		for (String tenantId : tenantIds) {
			tenantIdList.add(TenantId.create(tenantId));
		}
		Suser suser = Suser.createMember(userId, command.username(), command.password(), tenantIdList, command.createUserId());
		this.suserRepository.save(suser);
		return translator.translate(suser);
	}

	/**
	 * ユーザ名を変更します
	 * @param <R> 結果のクラス
	 * @param command ユーザ名変更コマンド
	 * @param translator 結果変換クラス
	 * @return 結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R changeName(
			SuserChangeNameCommand command,
			ApplicationTranslator<Suser, R> translator) throws DomainException {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}
		
		Suser suser = optSuser.get();
		suser.changeUsername(command.usernane(), command.updateUserId());
		
		this.suserRepository.update(suser);
		return translator.translate(suser);
	}
	
	/**
	 * パスワードを変更します
	 * @param <R> 結果のクラス
	 * @param command パスワード変更コマンド
	 * @param translator 結果変換クラス
	 * @return 結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R changePassword(SuserChangePasswordCommand command, ApplicationTranslator<Suser, R> translator) throws DomainException {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}
		Suser suser = optSuser.get();
		suser.changePassword(command.password(), command.updateUserId());
		
		this.suserRepository.update(suser);
		return translator.translate(suser);		
	}
	
	/**
	 * パスワードをリセットします
	 * @param rawUserId ユーザID
	 * @param updateUserId 更新者ID
	 * @return リセット後のパスワード
	 * @throws DomainException ドメインロジックエラー
	 */
	public String resetPasswordOf(String rawUserId, String updateUserId) throws DomainException {
		UserId userId = UserId.create(rawUserId);
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}

		Suser suser = optSuser.get();
		String password = suser.resetPassword(updateUserId);
		
		this.suserRepository.update(suser);
		return password;		
	}
	
	/**
	 * テナントの権限をユーザに付与します
	 * @param <R> 結果クラス
	 * @param command コマンド
	 * @param translator 変換クラス
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R grantTenant(
			SuserGrantTenantCommand command,
			ApplicationTranslator<Suser, R> translator) throws DomainException {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}

		Suser suser = optSuser.get();
		TenantId tenantId = TenantId.create(command.tenantId());
		if (!this.tenantDomainService.existTenant(tenantId)) {
			throw new DomainException(MessageCodes.nortis00003("テナント"));
		}
		suser.grantTenantAccressOf(tenantId);

		this.suserRepository.update(suser);
		return translator.translate(suser);
	}

	/**
	 * テナントの権限をユーザに付与します
	 * @param <R> 結果クラス
	 * @param command コマンド
	 * @param translator 変換クラス
	 * @return 処理結果
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R revokeTenant(
			SuserRevokeTenantCommand command,
			ApplicationTranslator<Suser, R> translator) throws DomainException {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}

		Suser suser = optSuser.get();
		TenantId tenantId = TenantId.create(command.tenantId());
		suser.revokeTenantAccessOf(tenantId);

		this.suserRepository.update(suser);
		return translator.translate(suser);
	}

	/**
	 * APIキーを作成します
	 * @param rawUserId ユーザID
	 * @return APIキー
	 * @throws DomainException ドメインロジックエラー
	 */
	public ApiKey createApiKey(String rawUserId) throws DomainException {
		UserId userId = UserId.create(rawUserId);
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}
		
		return this.authenticationDomainService.createApiKeyOf(optSuser.get());
	}

	/**
	 * ユーザを削除します
	 * @param rawUserId ユーザID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void deleteUserOf(String rawUserId) throws DomainException {
		UserId userId = UserId.create(rawUserId);
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException(MessageCodes.nortis00003("ユーザ"));
		}
		this.suserRepository.remove(optSuser.get());
	}

}
