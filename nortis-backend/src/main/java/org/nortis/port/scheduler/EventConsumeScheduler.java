package org.nortis.port.scheduler;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nortis.application.event.ReceiveEventApplicationService;
import org.nortis.infrastructure.message.MessageCodes;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * イベントを受信するスケジューラ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class EventConsumeScheduler {

    private final ReceiveEventApplicationService receiveEventApplicationService;

    private final MessageSource messageSource;

    /**
     * イベントの受信処理を実行します
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void startExpiredAuthentication() {
        try {
            this.receiveEventApplicationService.consume();
        } catch (Exception ex) {
            log.error(MessageCodes.nortis40001().resolveMessage(messageSource, Locale.getDefault()), ex);
        }
    }

}
