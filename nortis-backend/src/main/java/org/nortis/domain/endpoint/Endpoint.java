package org.nortis.domain.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.ToString;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.doma.entity.RootEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * エンドポイント
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Table(name = "ENDPOINT")
@Entity(listener = EndpointEntityListener.class, metamodel = @Metamodel)
public class Endpoint extends RootEntity {

    /**
     * テナントID
     */
    @Id
    @Column(name = "TENANT_ID")
    private TenantId tenantId;

    /**
     * エンドポイントID
     */
    @Id
    @Column(name = "ENDPOINT_ID")
    private EndpointId endpointId;

    /**
     * エンドポイント識別子
     */
    @Column(name = "ENDPOINT_IDENTIFIER")
    private EndpointIdentifier endpointIdentifier;

    /**
     * エンドポイント名
     */
    @Column(name = "ENDPOINT_NAME")
    private String endpointName;

    @Getter
    @Transient
    List<MessageTemplate> messageTemplateList = new ArrayList<>();

    /**
     * テンプレートを追加します
     * 
     * @param textType        テキストタイプ
     * @param subjectTemplate サブジェクトテンプレート
     * @param bodyTemplate    ボディテンプレート
     * @throws DomainException チェックエラー
     */
    public void createTemplate(TextType textType, SubjectTemplate subjectTemplate, BodyTemplate bodyTemplate)
            throws DomainException {
        Optional<MessageTemplate> optMessageTemplate = this.getTemplate(textType);
        if (optMessageTemplate.isPresent()) {
            throw new DomainException(MessageCodes.nortis20002());
        }
        MessageTemplate messageTemplate = MessageTemplate.create(this.endpointId, textType, subjectTemplate,
                bodyTemplate);
        messageTemplate.setInsert();
        messageTemplateList.add(messageTemplate);
    }

    /**
     * テンプレートを削除します
     * 
     * @param textType テキストタイプ
     */
    public void deleteTemplate(TextType textType) {
        Optional<MessageTemplate> optMessageTemplate = getTemplate(textType);
        optMessageTemplate.ifPresent(template -> {
            template.setDelete();
        });
    }

    /**
     * エンドポイント名を変更します
     * 
     * @param endpointName エンドポイント名
     * @throws DomainException ドメインロジックエラー
     */
    public void changeEndpointName(String endpointName) throws DomainException {
        setEndpointName(endpointName);
    }

    /**
     * テンプレートを取得します
     * 
     * @param textType テキストタイプ
     * @return テンプレート
     */
    public Optional<MessageTemplate> getTemplate(TextType textType) {
        //@formatter:off
        return this.messageTemplateList.stream()
                .filter(d -> d.getTextType().equals(textType) && !d.isDelete())
                .findFirst();
        //@formatteron
    }
    
    /**
     * エンドポイントID
     * 
     * @param endpointId エンドポイントID
     * @throws DomainException ドメインロジックエラー
     */
    public void setEndpointId(EndpointId endpointId) throws DomainException {
        Validations.notNull(endpointId, "エンドポイントID");
        this.endpointId = endpointId;
    }

    /**
     * テナントID
     * 
     * @param tenantId テナントID
     * @throws DomainException ドメインロジックエラー
     */
    public void setTenantId(TenantId tenantId) throws DomainException {
        Validations.notNull(tenantId, "テナントID");
        this.tenantId = tenantId;
    }

    /**
     * エンドポイント識別子
     * 
     * @param endpointIdentifier エンドポイント識別子
     * @throws DomainException ドメインロジックエラー
     */
    public void setEndpointIdentifier(EndpointIdentifier endpointIdentifier) throws DomainException {
        Validations.notNull(endpointIdentifier, "エンドポイント識別子");
        this.endpointIdentifier = endpointIdentifier;
    }

    /**
     * エンドポイント名
     * 
     * @param endpointName エンドポイント名
     * @throws DomainException ドメインロジックエラー
     */
    public void setEndpointName(String endpointName) throws DomainException {
        Validations.hasText(endpointName, "エンドポイント名");
        Validations.maxTextLength(endpointName, 50, "エンドポイント名");
        this.endpointName = endpointName;
    }

    /**
     * エンドポイントを新規作成します
     * 
     * @param endpointId         エンドポイントID
     * @param tenantId           テナントID
     * @param endpointIdentifier エンドポイント識別子
     * @param endpointName       エンドポイント名
     * @return 作成したエンドポイント
     * @throws DomainException ドメインロジックエラー
     */
    public static Endpoint create(TenantId tenantId, EndpointId endpointId, EndpointIdentifier endpointIdentifier,
            String endpointName) throws DomainException {
        Endpoint entity = new Endpoint();
        entity.setTenantId(tenantId);
        entity.setEndpointId(endpointId);
        entity.setEndpointIdentifier(endpointIdentifier);
        entity.setEndpointName(endpointName);
        return entity;
    }

}
