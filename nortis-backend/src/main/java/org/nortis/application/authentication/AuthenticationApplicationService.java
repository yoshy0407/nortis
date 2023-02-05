package org.nortis.application.authentication;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.domain.authentication.Authentication;
import org.nortis.domain.authentication.AuthenticationDomainService;
import org.nortis.domain.authentication.AuthenticationExpiredException;
import org.nortis.domain.authentication.AuthenticationFailureException;
import org.nortis.domain.authentication.AuthenticationRepository;
import org.nortis.domain.authentication.value.ApiKey;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 認証に関するアプリケーションサービスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@ApplicationService
public class AuthenticationApplicationService {

	private final AuthenticationRepository authenticationRepository;
	
	private final AuthenticationDomainService authenticationDomainService;
	
	/**
	 * ログイン処理を実施します
	 * @param <R> 任意のクラス
	 * @param userId ユーザID
	 * @param password パスワード
	 * @param translator 変換クラス
	 * @return 任意の型
	 * @throws DomainException ドメインロジックエラー
	 */
	public <R> R login(
			String userId, 
			String password, 
			ApplicationTranslator<Authentication, R> translator) throws DomainException {
		Authentication auth = 
				this.authenticationDomainService.login(UserId.create(userId), password);
		return translator.translate(auth);
	}
	
	/**
	 * ログアウトを実施します
	 * @param userId ユーザID
	 * @throws DomainException ドメインロジックエラー
	 */
	public void logout(String userId) throws DomainException {
		this.authenticationDomainService.logout(UserId.create(userId));
	}
	
	/**
	 * APIキーによる認可を実施します
	 * @param apiKey APIキー
	 * @return APIキーに紐づく{@link UserDetails}
	 * @throws AuthenticationExpiredException 期限切れの場合
	 * @throws AuthenticationFailureException 認証失敗の場合
	 */
	public NortisUserDetails authenticateOf(String apiKey) throws AuthenticationFailureException, AuthenticationExpiredException {
		try {
			return this.authenticationDomainService.authorizeOfApiKey(ApiKey.create(apiKey));
		} catch (DomainException e) {
			//桁数とかみて認証エラーメッセージを表示するのはよくないので、
			//このまま認証エラーとする
			throw new AuthenticationFailureException();
		}
	}
	
	/**
	 * 期限切れの認証を削除します
	 * @param baseDatetime 基準日時
	 */
	public void removeExpiredAuthentication(LocalDateTime baseDatetime) {
		List<Authentication> userAuthentications = this.authenticationRepository.getUserAuthentication();
		for (Authentication authentication : userAuthentications) {
			if (this.authenticationDomainService.checkExpired(authentication, baseDatetime)) {
				this.authenticationRepository.remove(authentication);
			}
		}
	}
}
