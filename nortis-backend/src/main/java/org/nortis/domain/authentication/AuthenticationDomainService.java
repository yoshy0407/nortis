package org.nortis.domain.authentication;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUser;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 認証に関するドメインサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
public class AuthenticationDomainService {

	private final AuthenticationRepository authenticationRepository;

	private final SuserRepository suserRepository;
	
	private final PasswordEncoder passwordEncoder;

	/** ユーザの認証の期限 */
	private final long expiredSecond;
	
	/**
	 * テナント用のAPIキーを作成します
	 * @param tenant テナント
	 * @return APIキー
	 */
	public ApiKey createApiKeyOf(Tenant tenant) {
		Optional<Authentication> optAuth = this.authenticationRepository
				.getFromTenantId(tenant.getTenantId());
		
		optAuth.ifPresent(data -> {
			this.authenticationRepository.remove(data);
		});
		
		Authentication auth = tenant.createApiKey();
		
		this.authenticationRepository.save(auth);
		return auth.getApiKey();
	}
	
	/**
	 * ユーザに紐づくAPIキーを作成します
	 * @param user ユーザ
	 * @return APIキー
	 */
	public ApiKey createApiKeyOf(Suser user) {
		Optional<Authentication> optAuth = this.authenticationRepository
				.getFromUserId(user.getUserId());
		
		optAuth.ifPresent(data -> {
			this.authenticationRepository.remove(data);
		});
		
		Authentication auth = user.createApiKey();
		
		this.authenticationRepository.save(auth);
		return auth.getApiKey();

	}
	
	/**
	 * 期限切れかどうかチェックします
	 * @param authentication 認証
	 * @param baseDatetime 基準日
	 * @return 期限切れかどうか
	 */
	public boolean checkExpired(Authentication authentication, LocalDateTime baseDatetime) {
		LocalDateTime lastAccessDatetime = authentication.getLastAccessDatetime();
		//nullの場合、認証が行われていないため、期限切れではない
		if (lastAccessDatetime == null) {
			return false;
		}
		return isExpired(authentication, baseDatetime);
	}
	
	/**
	 * 認可を実施します
	 * @param apiKey APIキー
	 * @return {@link NortisUserDetails}
	 * @throws AuthenticationFailureException 認証失敗の場合
	 * @throws AuthenticationExpiredException 期限切れの場合
	 */
	public NortisUserDetails authorizeOfApiKey(ApiKey apiKey) throws AuthenticationFailureException, AuthenticationExpiredException {
		Optional<Authentication> optAuth = this.authenticationRepository.get(apiKey);
		if (optAuth.isEmpty()) {
			throw new AuthenticationFailureException();
		}
		Authentication authentication = optAuth.get();
		//テナントの場合は、期限切れ気にせずに認証を実施
		if (authentication.getTenantId() != null) {
			authentication.setLastAccessDatetime(LocalDateTime.now());
			authenticationRepository.update(authentication);
			return NortisUser.createOfTenant(authentication);
		}
		if (authentication.getUserId() != null) {
			if (isExpired(authentication, LocalDateTime.now())) {
				//:TODO
				throw new AuthenticationExpiredException();
			} else {
				authentication.setLastAccessDatetime(LocalDateTime.now());
				authenticationRepository.update(authentication);
				Optional<Suser> optUser = this.suserRepository.get(authentication.getUserId());
				if (optUser.isEmpty()) {
					//:TODO
					throw new AuthenticationFailureException();					
				}
				return NortisUser.createOfUser(authentication, optUser.get());
			}			
		}
		//:TODO
		throw new AuthenticationFailureException();
	}
	
	/**
	 * ログインを実施します
	 * @param userId ユーザID
	 * @param password パスワード
	 * @return 認証
	 */
	public Authentication login(UserId userId, String password) {
		Optional<Suser> optUser = this.suserRepository.get(userId);
		if (optUser.isEmpty()) {
			throw new DomainException("MSG50004");
		}
		Suser suser = optUser.get();
		if (!passwordEncoder.matches(password, suser.getEncodedPassword())) {
			throw new DomainException("MSG50004");			
		}
		Authentication authentication = suser.login();
		
		this.authenticationRepository.save(authentication);
		return authentication;
	}
	
	/**
	 * ログアウト処理を実施します
	 * @param userId ユーザID
	 */
	public void logout(UserId userId) {
		Optional<Suser> optUser = this.suserRepository.get(userId);
		if (optUser.isEmpty()) {
			throw new DomainException("MSG00003", "ユーザ");
		}
		optUser.get().logout();
		
		Optional<Authentication> optAuth = this.authenticationRepository.getFromUserId(userId);
		//認証がなければ、正しい状態なのでそのままに
		optAuth.ifPresent(auth -> {
			this.authenticationRepository.remove(auth);
		});
	}
	
	private boolean isExpired(Authentication authentication, LocalDateTime baseDatetime) {
		if (authentication.getLastAccessDatetime() != null) {
			return baseDatetime.minusSeconds(expiredSecond)
					.isAfter(authentication.getLastAccessDatetime());
		} else {
			return false;
		}
	}
	
}
