package org.nortis.domain.endpoint;

import java.util.Map;
import lombok.Getter;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.infrastructure.doma.NortisEntityListener;
import org.nortis.infrastructure.doma.entity.AbstractEntity;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.template.TemplateRender;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

/**
 * メッセージテンプレート
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "MESSAGE_TEMPLATE")
@Entity(listener = NortisEntityListener.class, metamodel = @Metamodel)
public class MessageTemplate extends AbstractEntity {

    /**
     * エンドポイントID
     */
    @Id
    @Column(name = "ENDPOINT_ID")
    private EndpointId endpointId;

    /**
     * テキストタイプ
     */
    @Id
    @Column(name = "TEXT_TYPE")
    private TextType textType;

    /**
     * サブジェクトテンプレート
     */
    @Column(name = "SUBJECT_TEMPLATE")
    private SubjectTemplate subjectTemplate;

    /**
     * メッセージボディテンプレート
     */
    @Column(name = "BODY_TEMPLATE")
    private BodyTemplate bodyTemplate;

    @SuppressWarnings("javadoc")
    public void setEndpointId(EndpointId endpointId) throws DomainException {
        Validations.notNull(endpointId, "エンドポイントID");
        this.endpointId = endpointId;
    }

    @SuppressWarnings("javadoc")
    public void setTextType(TextType textType) throws DomainException {
        Validations.notNull(textType, "テキストタイプ");
        this.textType = textType;
    }

    @SuppressWarnings("javadoc")
    public void setSubjectTemplate(SubjectTemplate subjectTemplate) throws DomainException {
        Validations.notNull(subjectTemplate, "サブジェクトテンプレート");
        this.subjectTemplate = subjectTemplate;
    }

    @SuppressWarnings("javadoc")
    public void setBodyTemplate(BodyTemplate bodyTemplate) throws DomainException {
        Validations.notNull(bodyTemplate, "ボディテンプレート");
        this.bodyTemplate = bodyTemplate;
    }

    /**
     * テンプレートを更新します
     * 
     * @param subjectTemplate サブジェクトテンプレート
     * @param bodyTemplate    ボディテンプレート
     * @throws DomainException チェックエラー
     */
    public void updateTemplates(SubjectTemplate subjectTemplate, BodyTemplate bodyTemplate) throws DomainException {
        setSubjectTemplate(subjectTemplate);
        setBodyTemplate(bodyTemplate);
    }

    /**
     * パラメータからメッセージをレンダリングします
     * 
     * @param templateRender テンプレートレンダリング
     * @param parameters     パラメータ
     * @return 送信メッセージ
     */
    public RenderedMessage renderMessage(TemplateRender templateRender, Map<String, Object> parameters) {
        String subject = templateRender.render(this.endpointId.toString(), this.subjectTemplate.toString(), parameters);
        String body = templateRender.render(this.endpointId.toString(), this.bodyTemplate.toString(), parameters);
        return new RenderedMessage(textType, subject, body);
    }

    /**
     * 構築します
     * 
     * @param endpointId      エンドポイントID
     * @param textType        テキストタイプ
     * @param subjectTemplate サブジェクトテンプレート
     * @param bodyTemplate    ボディテンプレート
     * @return エンティティ
     * @throws DomainException チェックエラー
     */
    public static MessageTemplate create(EndpointId endpointId, TextType textType, SubjectTemplate subjectTemplate,
            BodyTemplate bodyTemplate) throws DomainException {
        MessageTemplate entity = new MessageTemplate();
        entity.setEndpointId(endpointId);
        entity.setTextType(textType);
        entity.setSubjectTemplate(subjectTemplate);
        entity.setBodyTemplate(bodyTemplate);
        return entity;
    }
}
