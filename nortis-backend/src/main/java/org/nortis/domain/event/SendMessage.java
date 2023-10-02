package org.nortis.domain.event;

import java.time.LocalDateTime;
import lombok.Getter;
import org.nortis.domain.endpoint.RenderedMessage;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.event.value.EventId;
import org.nortis.infrastructure.doma.entity.ImmutableEntity;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 * 送信するメッセージを表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */

@Getter
@Table(name = "SEND_MESSAGE")
@Entity(immutable = true, metamodel = @Metamodel)
public class SendMessage extends ImmutableEntity {

    @Id
    @Column(name = "EVENT_ID")
    private final EventId eventId;

    @Id
    @Column(name = "TEXT_TYPE")
    private final TextType textType;

    @Column(name = "SUBJECT")
    private final String subject;

    @Column(name = "BODY")
    private final String body;

    /**
     * コンストラクター
     * 
     * @param eventId  イベントID
     * @param textType テキストタイプ
     * @param subject  サブジェクト
     * @param body     ボディ
     * @param createId 作成ID
     * @param createDt 作成日付
     */
    public SendMessage(EventId eventId, TextType textType, String subject, String body, String createId,
            LocalDateTime createDt) {
        super(createId, createDt);
        this.eventId = eventId;
        this.textType = textType;
        this.subject = subject;
        this.body = body;
    }

    /**
     * 作成します
     * 
     * @param eventId イベントID
     * @param message メッセージ
     * @return {@link SendMessage}
     */
    public static SendMessage createFromMessage(EventId eventId, RenderedMessage message) {
        return new SendMessage(eventId, message.getTextType(), message.getSubject(), message.getBody(),
                SecurityContextUtils.getCurrentAuthorizedId().toString(), LocalDateTime.now());
    }

}
