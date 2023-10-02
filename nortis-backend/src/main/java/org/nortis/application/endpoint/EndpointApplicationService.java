package org.nortis.application.endpoint;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.nortis.application.endpoint.model.EndpointDeleteCommand;
import org.nortis.application.endpoint.model.EndpointDeleteMessageTemplateCommand;
import org.nortis.application.endpoint.model.EndpointMessageTemplateCommand;
import org.nortis.application.endpoint.model.EndpointNameUpdateCommand;
import org.nortis.application.endpoint.model.EndpointRegisterCommand;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.MessageTemplate;
import org.nortis.domain.endpoint.value.BodyTemplate;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.endpoint.value.SubjectTemplate;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * エンドポイントのアプリケーションサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Transactional
@AllArgsConstructor
@ApplicationService
public class EndpointApplicationService {

    /** テナントリポジトリ */
    private final TenantRepository tenantRepository;

    /** エンドポイントリポジトリ */
    private final EndpointRepository endpointRepository;

    private final NumberingDomainService numberingDomainService;

    /** 権限チェックサービス */
    private final AuthorityCheckDomainService authorityCheckDomainService;

    /**
     * テキストタイプを返します
     * 
     * @param <R>        変換後のクラス
     * @param translator 変換処理
     * @return テキストタイプのリスト
     */
    public <R> List<R> getTextTypes(ApplicationTranslator<TextType, R> translator) {
        //@formatter:off
        return Stream.of(TextType.values())
                .map(d -> translator.translate(d))
                .toList();
        //@formatter:on
    }

    /**
     * エンドポイントを登録します
     * 
     * @param <R>         結果のクラス
     * @param command     エンドポイント登録のコマンド
     * @param userDetails ユーザ
     * @param translator  結果を変換する処理
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R registerEndpoint(EndpointRegisterCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Endpoint, R> translator) throws DomainException {

        // 対象テナントの存在確認
        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndcheckTenant(tenantId);

        // 権限確認
        this.authorityCheckDomainService.checkHasWriteEndpoint(userDetails, tenant);

        // 作成処理
        EndpointId endpointId = this.numberingDomainService.createNewEndpointId();
        EndpointIdentifier endpointIdentifier = EndpointIdentifier.create(command.endpointIdentifier());

        Endpoint endpoint = tenant.createEndpoint(endpointId, endpointIdentifier, command.endpointName());

        TextType textType = TextType.resolve(command.textType());
        SubjectTemplate subjectTemplate = SubjectTemplate.create(command.subjectTemplate());
        BodyTemplate bodyTemplate = BodyTemplate.create(command.bodyTemplate());
        endpoint.createTemplate(textType, subjectTemplate, bodyTemplate);

        this.endpointRepository.save(endpoint);

        return translator.translate(endpoint);
    }

    /**
     * エンドポイント名を更新します
     * 
     * @param <R>         結果クラス
     * @param command     エンドポイント更新コマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R updateEndpointName(EndpointNameUpdateCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Endpoint, R> translator) throws DomainException {
        // テナントの存在確認
        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndcheckTenant(tenantId);

        // 権限確認
        this.authorityCheckDomainService.checkHasWriteEndpoint(userDetails, tenant);

        // エンドポイントの更新
        EndpointId endpointId = EndpointId.create(command.endpointId());
        Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
        if (optEndpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }

        Endpoint endpoint = optEndpoint.get();
        endpoint.changeEndpointName(command.endpointName());
        this.endpointRepository.update(endpoint);

        return translator.translate(endpoint);
    }

    /**
     * メッセージテンプレートを追加します
     * 
     * @param <R>         結果のクラス
     * @param command     コマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R addMessageTemplate(EndpointMessageTemplateCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Endpoint, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        getAndcheckTenant(tenantId);

        EndpointId endpointId = EndpointId.create(command.endpointId());
        Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
        if (optEndpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }

        TextType textType = TextType.resolve(command.textType());
        SubjectTemplate subjectTemplate = SubjectTemplate.create(command.subjectTemplate());
        BodyTemplate bodyTemplate = BodyTemplate.create(command.bodyTemplate());
        Endpoint endpoint = optEndpoint.get();

        endpoint.createTemplate(textType, subjectTemplate, bodyTemplate);

        this.endpointRepository.update(endpoint);

        return translator.translate(endpoint);
    }

    /**
     * メッセージテンプレートを更新します
     * 
     * @param <R>         結果クラス
     * @param command     コマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R updateMessageTemplate(EndpointMessageTemplateCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Endpoint, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        getAndcheckTenant(tenantId);

        EndpointId endpointId = EndpointId.create(command.endpointId());
        Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
        if (optEndpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }

        TextType textType = TextType.resolve(command.textType());
        SubjectTemplate subjectTemplate = SubjectTemplate.create(command.subjectTemplate());
        BodyTemplate bodyTemplate = BodyTemplate.create(command.bodyTemplate());
        Endpoint endpoint = optEndpoint.get();

        Optional<MessageTemplate> optMessageTemplate = endpoint.getTemplate(textType);
        if (optMessageTemplate.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20003());
        }
        optMessageTemplate.get().updateTemplates(subjectTemplate, bodyTemplate);

        this.endpointRepository.update(endpoint);

        return translator.translate(endpoint);
    }

    /**
     * メッセージテンプレートを削除します
     * 
     * @param command     コマンド
     * @param userDetails 認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    public void deleteMessageTemplate(EndpointDeleteMessageTemplateCommand command, NortisUserDetails userDetails)
            throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        getAndcheckTenant(tenantId);

        EndpointId endpointId = EndpointId.create(command.endpointId());
        Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenantId, endpointId);
        if (optEndpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }

        TextType textType = TextType.resolve(command.textType());
        Endpoint endpoint = optEndpoint.get();

        endpoint.deleteTemplate(textType);

        this.endpointRepository.update(endpoint);
    }

    /**
     * エンドポイントを削除します
     * 
     * @param command     削除コマンド
     * @param userDetails ユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public void delete(EndpointDeleteCommand command, NortisUserDetails userDetails) throws DomainException {
        // テナントの存在確認
        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndcheckTenant(tenantId);

        // テナントの権限確認
        this.authorityCheckDomainService.checkHasWriteEndpoint(userDetails, tenant);

        // エンドポイントの確認
        EndpointId endpointId = EndpointId.create(command.endpointId());

        Optional<Endpoint> optEndpoint = this.endpointRepository.get(tenant.getTenantId(), endpointId);
        if (optEndpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }

        // 削除処理
        Endpoint endpoint = optEndpoint.get();
        this.endpointRepository.remove(endpoint);
    }

    /**
     * 指定されたテナントのエンドポイントを削除します
     * 
     * @param tenantId テナントID
     * @throws DomainException ドメインロジックエラー
     */
    public void deleteFromTenantId(String tenantId) throws DomainException {
        TenantId id = TenantId.create(tenantId);
        List<Endpoint> endpointList = this.endpointRepository.getFromTenantId(id);
        this.endpointRepository.removeAll(endpointList);
    }

    /**
     * テナントの存在チェックを実施します
     * 
     * @param rawTenantId テナントID
     * @return テナント
     * @throws DomainException ロジックエラー
     */
    private Tenant getAndcheckTenant(TenantId tenantId) throws DomainException {
        // 対象テナントの存在確認
        Optional<Tenant> tenant = this.tenantRepository.getByTenantId(tenantId);

        if (tenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }
        return tenant.get();
    }

}
