package org.nortis.domain.service;

import java.util.ArrayList;
import java.util.List;
import org.nortis.domain.service.password.PasswordPolicy;
import org.nortis.domain.user.value.HashedPassword;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.utils.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * パスワードのドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@DomainService
public class PasswordDomainService {

    private final List<PasswordPolicy> specificationList;

    private final PasswordEncoder passwordEncoder;

    /**
     * インスタンスを生成します
     * 
     * @param passwordEncoder {@link PasswordEncoder}
     */
    public PasswordDomainService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.specificationList = new ArrayList<>();
    }

    /**
     * パスワードポリシーをチェックします
     * 
     * @param rawPassword パスワード
     * @throws DomainException ビジネスロジックエラー
     */
    public void checkPolicyOf(String rawPassword) throws DomainException {
        for (PasswordPolicy spec : this.specificationList) {
            if (!spec.isSatisfied(rawPassword)) {
                throw new DomainException(spec.getMessageCode());
            }
        }
    }

    /**
     * パスワードをハッシュ化します
     * 
     * @param rawPassword パスワード
     * @return ハッシュ化したパスワード
     */
    public HashedPassword hashPassword(String rawPassword) {
        return HashedPassword.create(this.passwordEncoder.encode(rawPassword));
    }

    /**
     * パスワードが一致するかチェックします
     * 
     * @param rawPassword    パスワード
     * @param hashedPassword ハッシュ化されたパスワード
     * @return 確認結果
     */
    public boolean match(String rawPassword, HashedPassword hashedPassword) {
        return this.passwordEncoder.matches(rawPassword, hashedPassword.toString());
    }

    /**
     * リセットしたパスワードを作成します
     * 
     * @return パスワード
     */
    public String createResetPassword() {
        return RandomString.of(15).build();
    }

    /**
     * ポリシーを追加します
     * 
     * @param policy ポリシー
     */
    public void addPolicy(PasswordPolicy policy) {
        this.specificationList.add(policy);
    }

}
