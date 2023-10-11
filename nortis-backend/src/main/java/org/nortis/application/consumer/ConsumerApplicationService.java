package org.nortis.application.consumer;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.application.consumer.model.ConsumerDeleteCommand;
import org.nortis.application.consumer.model.ConsumerRegisterCommand;
import org.nortis.application.consumer.model.ConsumerSubscribeCommand;
import org.nortis.application.consumer.model.ConsumerUpdateCommand;
import org.nortis.domain.consumer.Consumer;
import org.nortis.domain.consumer.ConsumerRepository;
import org.nortis.domain.consumer.value.ConsumerId;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.ConsumerDomainService;
import org.nortis.domain.service.NumberingDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * メールコンシューマのアプリケーションサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Transactional
@AllArgsConstructor
@ApplicationService
public class ConsumerApplicationService {

    private final TenantRepository tenantRepository;

    private final ConsumerRepository consumerRepository;

    private final EndpointRepository endpointRepository;

    private final AuthorityCheckDomainService authorityCheckDomainService;

    private final ConsumerDomainService consumerDomainService;

    private final NumberingDomainService numberingDomainService;

    /**
     * コンシューマを取得します
     * 
     * @param <R>           結果
     * @param rawTenantId   テナントID
     * @param rawConsumerId コンシューマID
     * @param user          認証ユーザ
     * @param translator    変換処理
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R get(String rawTenantId, String rawConsumerId, NortisUserDetails user,
            ApplicationTranslator<Consumer, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(rawTenantId);
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(user, tenant);

        ConsumerId consumerId = ConsumerId.create(rawConsumerId);
        Consumer consumer = getAndCheckConsumer(tenantId, consumerId);
        return translator.translate(consumer);
    }

    /**
     * ページングでデータを取得します
     * 
     * @param <R>         結果
     * @param rawTenantId テナントID
     * @param paging      ページング
     * @param user        認証ユーザ
     * @param translator  変換処理
     * @return レスポンス
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> List<R> getListPaging(String rawTenantId, Paging paging, NortisUserDetails user,
            ApplicationTranslator<Consumer, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(rawTenantId);
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(user, tenant);

        List<Consumer> consumerList = this.consumerRepository.getListPaging(tenantId, paging);
        //@formatter:off
        return consumerList.stream()
                .map(d -> translator.translate(d))
                .toList();
        //@formatter:on
    }

    /**
     * メールコンシューマを登録します
     * 
     * @param <R>         結果クラス
     * @param command     メールコンシューマの登録コマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 処理結果
     * @throws DomainException ドメインロジックエラー
     */
    public <R> R register(ConsumerRegisterCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Consumer, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(userDetails, tenant);

        // パラメータチェック
        this.consumerDomainService.validateConsumerParameter(command.consumerType(), command.parameter());

        ConsumerId consumerId = this.numberingDomainService.createNewConsumerId();
        TextType textType = TextType.resolve(command.textType());
        Consumer consumer = tenant.createConsumer(consumerId, command.consumerName(), command.consumerType(), textType,
                command.parameter());

        this.consumerRepository.save(consumer);

        return translator.translate(consumer);
    }

    /**
     * コンシューマを更新します
     * 
     * @param <R>         結果クラス
     * @param command     更新コマンド
     * @param userDetails 認証ユーザ
     * @param translator  変換処理
     * @return 結果
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R update(ConsumerUpdateCommand command, NortisUserDetails userDetails,
            ApplicationTranslator<Consumer, R> translator) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(userDetails, tenant);

        // パラメータチェック
        this.consumerDomainService.validateConsumerParameter(command.consumerType(), command.parameter());

        ConsumerId consumerId = ConsumerId.create(command.consumerId());
        Consumer consumer = getAndCheckConsumer(tenantId, consumerId);

        consumer.changeName(command.consumerName());
        consumer.changeTextType(TextType.resolve(command.textType()));
        consumer.updateParameter(command.parameter());

        this.consumerRepository.update(consumer);

        return translator.translate(consumer);
    }

    /**
     * 指定されたエンドポイントをサブスクライブします
     * 
     * @param command     コマンド
     * @param userDetails 認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    public void subscribe(ConsumerSubscribeCommand command, NortisUserDetails userDetails) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(userDetails, tenant);

        EndpointId endpointId = EndpointId.create(command.endpointId());
        getAndCheckEndpoint(tenantId, endpointId);

        ConsumerId consumerId = ConsumerId.create(command.consumerId());
        Consumer consumer = getAndCheckConsumer(tenantId, consumerId);

        consumer.subscribe(endpointId);

        consumerRepository.update(consumer);
    }

    /**
     * 指定されたエンドポイントのサブスクライブを外します
     * 
     * @param command     コマンド
     * @param userDetails 認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    public void unsubscribe(ConsumerSubscribeCommand command, NortisUserDetails userDetails) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(userDetails, tenant);

        EndpointId endpointId = EndpointId.create(command.endpointId());
        getAndCheckEndpoint(tenantId, endpointId);

        ConsumerId consumerId = ConsumerId.create(command.consumerId());
        Consumer consumer = getAndCheckConsumer(tenantId, consumerId);

        consumer.unsubscribe(endpointId);

        consumerRepository.update(consumer);
    }

    /**
     * 削除します
     * 
     * @param command     削除コマンド
     * @param userDetails 認証ユーザ
     * @throws DomainException ドメインロジックエラー
     */
    public void delete(ConsumerDeleteCommand command, NortisUserDetails userDetails) throws DomainException {

        TenantId tenantId = TenantId.create(command.tenantId());
        Tenant tenant = getAndCheckTenant(tenantId);

        this.authorityCheckDomainService.checkHasWriteConsumer(userDetails, tenant);

        ConsumerId consumerId = ConsumerId.create(command.consumerId());

        Consumer consumer = getAndCheckConsumer(tenantId, consumerId);

        this.consumerRepository.remove(consumer);
    }

    /**
     * エンドポイント削除による対象レコードのエンドポイント設定を削除します
     * 
     * @param rawEndpointId エンドポイントID
     * 
     * @throws DomainException ドメインロジックエラー
     */
    public void unsubscribeEndpoint(String rawEndpointId) throws DomainException {
        EndpointId endpointId = EndpointId.create(rawEndpointId);
        List<Consumer> consumers = this.consumerRepository.getFromEndpoint(endpointId);

        if (consumers.isEmpty()) {
            return;
        }

        consumers.forEach(data -> {
            data.unsubscribe(endpointId);
        });

        this.consumerRepository.updateAll(consumers);
    }

    private Tenant getAndCheckTenant(TenantId tenantId) throws DomainException {
        // 対象テナントの存在確認
        Optional<Tenant> tenant = this.tenantRepository.getByTenantId(tenantId);

        if (tenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }
        return tenant.get();
    }

    private Consumer getAndCheckConsumer(TenantId tenantId, ConsumerId consumerId) throws DomainException {
        Optional<Consumer> consumer = this.consumerRepository.get(tenantId, consumerId);
        if (consumer.isEmpty()) {
            throw new DomainException(MessageCodes.nortis30002());
        }
        return consumer.get();
    }

    private Endpoint getAndCheckEndpoint(TenantId tenantId, EndpointId endpointId) throws DomainException {
        Optional<Endpoint> endpoint = this.endpointRepository.get(tenantId, endpointId);
        if (endpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }
        return endpoint.get();
    }

}
