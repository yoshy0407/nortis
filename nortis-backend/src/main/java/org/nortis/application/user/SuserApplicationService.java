package org.nortis.application.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

/**
 * ユーザのアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class SuserApplicationService {

	private final SuserRepository suserRepository;
	
	private final AuthenticationDomainService authenticationDomainService;
	
	/**
	 * 管理者ユーザを登録します
	 * @param <R> 結果クラス
	 * @param command ユーザ登録コマンド
	 * @param translator 結果変換クラス
	 * @return 結果
	 */
	public <R> R registerAdminUser(SuserRegisterCommand command, ApplicationTranslator<Suser, R> translator) {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isPresent()) {
			throw new DomainException("MSG50003");
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
	 */
	public <R> R registerMemberUser(
			SuserRegisterCommand command, 
			String[] tenantIds,
			ApplicationTranslator<Suser, R> translator) {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isPresent()) {
			throw new DomainException("MSG50003");
		}
		
		List<TenantId> tenantIdList = Stream.of(tenantIds).map(s -> TenantId.create(s)).toList();
		Suser suser = Suser.createMember(userId, command.username(), command.password(), tenantIdList, command.userId());
		this.suserRepository.save(suser);
		return translator.translate(suser);
	}

	/**
	 * ユーザ名を変更します
	 * @param <R> 結果のクラス
	 * @param command ユーザ名変更コマンド
	 * @param translator 結果変換クラス
	 * @return 結果
	 */
	public <R> R changeName(
			SuserChangeNameCommand command,
			ApplicationTranslator<Suser, R> translator) {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
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
	 */
	public <R> R changePassword(SuserChangePasswordCommand command, ApplicationTranslator<Suser, R> translator) {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
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
	 */
	public String resetPasswordOf(String rawUserId, String updateUserId) {
		UserId userId = UserId.create(rawUserId);
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
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
	 */
	public <R> R grantTenant(
			SuserGrantTenantCommand command,
			ApplicationTranslator<Suser, R> translator) {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
		}

		Suser suser = optSuser.get();
		TenantId tenantId = TenantId.create(command.tenantId());
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
	 */
	public <R> R revokeTenant(
			SuserRevokeTenantCommand command,
			ApplicationTranslator<Suser, R> translator) {
		UserId userId = UserId.create(command.userId());
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
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
	 */
	public ApiKey createApiKey(String rawUserId) {
		UserId userId = UserId.create(rawUserId);
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
		}
		
		return this.authenticationDomainService.createApiKeyOf(optSuser.get());
	}

	/**
	 * ユーザを削除します
	 * @param rawUserId ユーザID
	 */
	public void deleteUserOf(String rawUserId) {
		UserId userId = UserId.create(rawUserId);
		Optional<Suser> optSuser = this.suserRepository.get(userId);
		if (optSuser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
		}
		this.suserRepository.remove(optSuser.get());
	}

}
