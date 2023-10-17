package org.nortis.domain.event.value;

import java.io.Serializable;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.exception.UnexpectedException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Domain;

/**
 * イベントID
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@EqualsAndHashCode
@Domain(valueType = String.class, accessorMethod = "toString", factoryMethod = "createOfDoma")
public final class EventId implements Serializable {

    private static final long serialVersionUID = 1L;

    /** イベントIDの値 */
    private final String value;

    /**
     * コンストラクター
     * 
     * @param eventId イベントID
     * @throws DomainException ドメインロジックエラー
     */
    private EventId(String eventId) throws DomainException {
        Validations.hasText(eventId, "イベントID");
        this.value = eventId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 値を元にイベントIDオブジェクトを生成します
     * 
     * @param eventId イベントID
     * @return イベントIDオブジェクト
     * @throws DomainException ドメインロジックエラー
     */
    public static EventId create(String eventId) throws DomainException {
        return new EventId(eventId);
    }

    /**
     * 新しい値を採番します
     * 
     * @return イベントID
     * @throws DomainException ドメインロジックエラー
     */
    public static EventId createNew() throws DomainException {
        return new EventId(UUID.randomUUID().toString());
    }

    /**
     * Domaのファクトリメソッド
     * 
     * @param eventId イベントID
     * @return イベントID
     * @throws DomainException ドメインロジックエラー
     */
    public static EventId createOfDoma(String eventId) {
        try {
            return create(eventId);
        } catch (DomainException e) {
            throw UnexpectedException.convertDomainException(e);
        }
    }

}
