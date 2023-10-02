package org.nortis.port.listener;

import lombok.extern.slf4j.Slf4j;
import org.nortis.infrastructure.event.AbstractEvent;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.springframework.context.event.EventListener;

/**
 * 抽象的なイベントリスナーです
 * 
 * @author yoshiokahiroshi
 * @param <E> イベント
 * @version 1.0.0
 */
@Slf4j
public abstract class AbstractEventListener<E extends AbstractEvent> {

    /**
     * Springから呼び出される受信処理です
     * 
     * @param event イベント
     */
    @EventListener
    public void subscribe(E event) {
        SecurityContextUtils.setAuthentication(event.getAuthentication());
        try {
            doSubscribe(event);
        } catch (Exception ex) {
            log.error("%sの受信に失敗しました".formatted(getProcessName()), ex);
        }
    }

    /**
     * 受信処理を実行します
     * 
     * @param event イベント
     * @throws Exception エラー
     */
    protected abstract void doSubscribe(E event) throws Exception;

    /**
     * 処理名を返却します
     * 
     * @return 処理名
     */
    protected abstract String getProcessName();
}
