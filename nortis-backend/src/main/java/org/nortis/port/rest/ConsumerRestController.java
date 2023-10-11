package org.nortis.port.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.nortis.application.consumer.ConsumerApplicationService;
import org.nortis.application.consumer.model.ConsumerDeleteCommand;
import org.nortis.application.consumer.model.ConsumerRegisterCommand;
import org.nortis.application.consumer.model.ConsumerSubscribeCommand;
import org.nortis.application.consumer.model.ConsumerUpdateCommand;
import org.nortis.domain.consumer.Consumer;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.port.rest.payload.ConsumerCreateRequest;
import org.nortis.port.rest.payload.ConsumerSubscribeRequest;
import org.nortis.port.rest.payload.ConsumerUpdateRequest;
import org.nortis.port.rest.resource.ConsumerResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * コンシューマに関するコントローラ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RestController
@RequestMapping("/tenant/{tenantId}/consumer")
public class ConsumerRestController {

    private final ConsumerApplicationService consumerApplicationService;

    /**
     * IDで取得します
     * 
     * @param tenantId   テナントID
     * @param consumerId コンシューマID
     * @param user       認証ユーザ
     * @return レスポンス
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{consumerId}")
    public ConsumerResource get(@PathVariable String tenantId, @PathVariable String consumerId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.consumerApplicationService.get(tenantId, consumerId, user, translator());
    }

    /**
     * ページングで取得します
     * 
     * @param tenantId    テナントID
     * @param pageNo      ページ番号
     * @param pagePersize ページあたりの件数
     * @param user        認証ユーザ
     * @return レスポンス
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ConsumerResource> getListPaging(@PathVariable String tenantId, @RequestParam int pageNo,
            @RequestParam int pagePersize, @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        return this.consumerApplicationService.getListPaging(tenantId, new Paging(pageNo, pagePersize), user,
                translator());
    }

    /**
     * コンシューマ作成API
     * 
     * @param tenantId テナントID
     * @param request  リクエスト
     * @param user     認証ユーザ
     * @return レスポンス
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ConsumerResource create(@PathVariable String tenantId, @RequestBody @Validated ConsumerCreateRequest request,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {

        //@formatter:off
        Map<String, String> parameter = request.getParameters().stream()
                .collect(Collectors.toUnmodifiableMap(d -> d.getName(), d -> d.getValue()));
        
        ConsumerRegisterCommand command = ConsumerRegisterCommand.builder()
                .tenantId(tenantId)
                .consumerName(request.getConsumerName())
                .consumerType(request.getConsumerType())
                .textType(request.getTextType())
                .parameter(parameter)
                .build();
        //@formatter:on

        return this.consumerApplicationService.register(command, user, translator());
    }

    /**
     * コンシューマを更新します
     * 
     * @param tenantId   テナントID
     * @param consumerId コンシューマID
     * @param request    リクエスト
     * @param user       認証ユーザ
     * @return レスポンス
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{consumerId}")
    public ConsumerResource update(@PathVariable String tenantId, @PathVariable String consumerId,
            @RequestBody @Validated ConsumerUpdateRequest request, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        //@formatter:off
        Map<String, String> parameter = request.getParameters().stream()
                .collect(Collectors.toUnmodifiableMap(d -> d.getName(), d -> d.getValue()));

        ConsumerUpdateCommand command = ConsumerUpdateCommand.builder()
                .tenantId(tenantId)
                .consumerId(consumerId)
                .consumerName(request.getConsumerName())
                .consumerType(request.getConsumerType())
                .textType(request.getTextType())
                .parameter(parameter)
                .build();
        //@formatter:on

        return this.consumerApplicationService.update(command, user, translator());
    }

    /**
     * コンシューマをサブスクライブします
     * 
     * @param tenantId   テナントID
     * @param consumerId コンシューマID
     * @param request    リクエスト
     * @param user       認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{consumerId}/subscribe")
    public void subscribe(@PathVariable String tenantId, @PathVariable String consumerId,
            @RequestBody @Validated ConsumerSubscribeRequest request, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        //@formatter:off
        ConsumerSubscribeCommand command = ConsumerSubscribeCommand.builder()
                .tenantId(tenantId)
                .consumerId(consumerId)
                .endpointId(request.getEndpointId())
                .build();
        //@formatter:on

        this.consumerApplicationService.subscribe(command, user);
    }

    /**
     * コンシューマをアンサブスクライブします
     * 
     * @param tenantId   テナントID
     * @param consumerId コンシューマID
     * @param request    リクエスト
     * @param user       認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("{consumerId}/unsubscribe")
    public void unsubscribe(@PathVariable String tenantId, @PathVariable String consumerId,
            @RequestBody @Validated ConsumerSubscribeRequest request, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        //@formatter:off
        ConsumerSubscribeCommand command = ConsumerSubscribeCommand.builder()
                .tenantId(tenantId)
                .consumerId(consumerId)
                .endpointId(request.getEndpointId())
                .build();
        //@formatter:on

        this.consumerApplicationService.unsubscribe(command, user);
    }

    /**
     * コンシューマを削除します
     * 
     * @param tenantId   テナントID
     * @param consumerId コンシューマID
     * @param user       認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @DeleteMapping("{consumerId}")
    public void delete(@PathVariable String tenantId, @PathVariable String consumerId,
            @AuthenticationPrincipal NortisUserDetails user) throws DomainException {
        //@formatter:off
        ConsumerDeleteCommand command = ConsumerDeleteCommand.builder()
                .tenantId(tenantId)
                .consumerId(consumerId)
                .build();
        //@formatter:on

        this.consumerApplicationService.delete(command, user);
    }

    private ApplicationTranslator<Consumer, ConsumerResource> translator() {
        return consumer -> {
          //@formatter:off
          return ConsumerResource.builder()
                  .tenantId(consumer.getTenantId().toString())
                  .consumerId(consumer.getConsumerId().toString())
                  .consumerName(consumer.getConsumerName())
                  .consumerType(consumer.getConsumerTypeCode())
                  .textType(consumer.getTextType().getTextType())
                  .build();  
          //@formatter:on
        };
    }

}
