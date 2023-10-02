package org.nortis.domain.consumer;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.infrastructure.doma.NortisEntityListener;
import org.nortis.infrastructure.doma.entity.AbstractEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 * パラメータ定義
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@ToString
@Entity(metamodel = @Metamodel, listener = NortisEntityListener.class)
@Table(name = "CONSUMER_PARAMETER")
public class ConsumerParameter extends AbstractEntity {

    /**
     * コンシューマID
     */
    @Id
    @Column(name = "CONSUMER_ID")
    private ConsumerId consumerId;

    /**
     * パラメータ名
     */
    @Id
    @Column(name = "PARAMETER_NAME")
    private String parameterName;

    /**
     * パラメータ値
     */
    @Setter
    @Column(name = "PARAMETER_VALUE")
    private String parameterValue;

    @SuppressWarnings("javadoc")
    public void setConsumerId(ConsumerId consumerId) throws DomainException {
        Validations.notNull(consumerId, "コンシューマID");
        this.consumerId = consumerId;
    }

    @SuppressWarnings("javadoc")
    public void setParameterName(String parameterName) throws DomainException {
        Validations.notNull(parameterName, "パラメータ名");
        this.parameterName = parameterName;
    }

    /**
     * 構築します
     * 
     * @param consumerId     コンシューマID
     * @param parameterName  パラメータ名
     * @param parameterValue パラメータ値
     * @return {@link ConsumerParameter}
     * @throws DomainException チェックエラー
     */
    public static ConsumerParameter create(ConsumerId consumerId, String parameterName, String parameterValue)
            throws DomainException {
        ConsumerParameter entity = new ConsumerParameter();
        entity.setConsumerId(consumerId);
        entity.setParameterName(parameterName);
        entity.setParameterValue(parameterValue);
        entity.setCreateId(SecurityContextUtils.getCurrentAuthorizedId().toString());
        entity.setCreateDt(LocalDateTime.now());
        entity.setVersion(1L);
        return entity;
    }

}
