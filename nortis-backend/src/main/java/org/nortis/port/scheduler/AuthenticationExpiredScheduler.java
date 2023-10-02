package org.nortis.port.scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.authentication.AuthenticationApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ログインの有効期限切れをチェックするスケジューラ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class AuthenticationExpiredScheduler {

    private final AuthenticationApplicationService authenticationApplicationService;

    /**
     * ログインの有効期限切れのスケジューリング処理を実行します
     */
    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public void startExpiredAuthentication() {
        LocalDateTime baseDateTime = LocalDateTime.now().minusMinutes(30);
        try {
            this.authenticationApplicationService.removeExpiredAuthentication(baseDateTime);
        } catch (Exception ex) {
            log.error("ログインの有効期限切れの処理に失敗しました", ex);
        }
    }

}
