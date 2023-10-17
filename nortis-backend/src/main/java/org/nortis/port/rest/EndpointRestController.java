package org.nortis.port.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.nortis.application.endpoint.model.EndpointDeleteCommand;
import org.nortis.application.endpoint.model.EndpointDeleteMessageTemplateCommand;
import org.nortis.application.endpoint.model.EndpointMessageTemplateCommand;
import org.nortis.application.endpoint.model.EndpointNameUpdateCommand;
import org.nortis.application.endpoint.model.EndpointRegisterCommand;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.application.Paging;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.nortis.port.rest.payload.EndpointCreateRequest;
import org.nortis.port.rest.payload.EndpointMessageTemplateRequest;
import org.nortis.port.rest.payload.EndpointNameUpdateRequest;
import org.nortis.port.rest.resource.EndpointResource;
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
 * エンドポイントのコントローラです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@RequestMapping("/tenant/{tenantId}")
@RestController
@AllArgsConstructor
public class EndpointRestController {

    private final EndpointApplicationService endpointApplicationService;

    /**
     * エンドポイントを取得します
     * 
     * @param tenantId    テナントID
     * @param endpointId  エンドポイントID
     * @param userDetails 認証ユーザ
     * @return レスポンス
     * @throws DomainException チェックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/endpoint/{endpointId}")
    public EndpointResource getEndpoint(@PathVariable String tenantId, @PathVariable String endpointId,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        return this.endpointApplicationService.getEndpoint(tenantId, endpointId, userDetails, translator());
    }

    /**
     * エンドポイントを複数件取得します
     * 
     * @param tenantId    テナントID
     * @param pageNo      ページ番号
     * @param pagePerSize １ページ当たりのサイズ
     * @param userDetails 認証ユーザ
     * @return 取得結果
     * @throws DomainException ビジネスロジックエラー
     */
    @GetMapping("/endpoint")
    public List<EndpointResource> getList(@PathVariable String tenantId, @RequestParam int pageNo,
            @RequestParam int pagePerSize, @AuthenticationPrincipal NortisUserDetails userDetails)
            throws DomainException {
        return this.endpointApplicationService.getEndpointList(tenantId, new Paging(pageNo, pagePerSize), userDetails,
                translator());
    }

    /**
     * エンドポイントを登録します
     * 
     * @param tenantId テナントID
     * @param request  リクエスト
     * @param user     認証ユーザ
     * @return リソース
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/endpoint")
    public EndpointResource createEndpoint(@PathVariable String tenantId,
            @RequestBody @Validated EndpointCreateRequest request, @AuthenticationPrincipal NortisUserDetails user)
            throws DomainException {
        //@formatter:off
        EndpointRegisterCommand command = EndpointRegisterCommand.builder()
                .tenantId(tenantId)
                .endpointIdentifier(request.getEndpointIdentifier())
                .endpointName(request.getEndpointName())
                .textType(request.getTextType())
                .subjectTemplate(request.getSubjectTemplate())
                .bodyTemplate(request.getBodyTemplate())
                .build();
        //@formatter:on
        return this.endpointApplicationService.registerEndpoint(command, user, translator());
    }

    /**
     * エンドポイント名を更新します
     * 
     * @param tenantId    テナントID
     * @param endpointId  エンドポイントID
     * @param request     リクエスト
     * @param userDetails 認証ユーザ
     * @return エンドポイントリソース
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/endpoint/{endpointId}/name")
    public EndpointResource updateEndpointName(@PathVariable String tenantId, @PathVariable String endpointId,
            @RequestBody @Validated EndpointNameUpdateRequest request,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        EndpointNameUpdateCommand command = new EndpointNameUpdateCommand(tenantId, endpointId,
                request.getEndpointName());
        return this.endpointApplicationService.updateEndpointName(command, userDetails, translator());
    }

    /**
     * メッセージテンプレートを追加します
     * 
     * @param tenantId    テナントID
     * @param endpointId  エンドポイントID
     * @param textType    テキストタイプ
     * @param request     リクエスト
     * @param userDetails 認証ユーザ
     * @return エンドポイントのリソース
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/endpoint/{endpointId}/message-template")
    public EndpointResource addMessageTemplate(@PathVariable String tenantId, @PathVariable String endpointId,
            @PathVariable String textType, @RequestBody @Validated EndpointMessageTemplateRequest request,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        //@formatter:off
        EndpointMessageTemplateCommand command = EndpointMessageTemplateCommand.builder()
                .tenantId(tenantId)
                .endpointId(endpointId)
                .textType(textType)
                .subjectTemplate(request.getSubjectTemplate())
                .bodyTemplate(request.getBodyTemplate())
                .build();
        //@formatter:on
        return this.endpointApplicationService.addMessageTemplate(command, userDetails, translator());
    }

    /**
     * メッセージテンプレートを更新します
     * 
     * @param tenantId    テナントID
     * @param endpointId  エンドポイントID
     * @param textType    テキストタイプ
     * @param request     リクエスト
     * @param userDetails 認証ユーザ
     * @return エンドポイントリソース
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/endpoint/{endpointId}/message-template")
    public EndpointResource updateMessageTemplate(@PathVariable String tenantId, @PathVariable String endpointId,
            @PathVariable String textType, @RequestBody @Validated EndpointMessageTemplateRequest request,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        //@formatter:off
        EndpointMessageTemplateCommand command = EndpointMessageTemplateCommand.builder()
                .tenantId(tenantId)
                .endpointId(endpointId)
                .textType(textType)
                .subjectTemplate(request.getSubjectTemplate())
                .bodyTemplate(request.getBodyTemplate())
                .build();
        //@formatter:on
        return this.endpointApplicationService.updateMessageTemplate(command, userDetails, translator());
    }

    /**
     * メッセージテンプレートを削除します
     * 
     * @param tenantId    テナントID
     * @param endpointId  エンドポイントID
     * @param textType    テキストタイプ
     * @param userDetails 認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/endpoint/{endpointId}/message-template/{textType}")
    public void deleteMessageTemplate(@PathVariable String tenantId, @PathVariable String endpointId,
            @PathVariable String textType, @AuthenticationPrincipal NortisUserDetails userDetails)
            throws DomainException {
        EndpointDeleteMessageTemplateCommand command = new EndpointDeleteMessageTemplateCommand(tenantId, endpointId,
                textType);
        endpointApplicationService.deleteMessageTemplate(command, userDetails);
    }

    /**
     * エンドポイントを削除します
     * 
     * @param tenantId    テナントID
     * @param endpointId  エンドポイントID
     * @param userDetails 認証ユーザ
     * @throws DomainException ビジネスロジックエラー
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/endpoint/{endpointId}")
    public void delete(@PathVariable String tenantId, @PathVariable String endpointId,
            @AuthenticationPrincipal NortisUserDetails userDetails) throws DomainException {
        EndpointDeleteCommand command = new EndpointDeleteCommand(tenantId, endpointId);
        this.endpointApplicationService.delete(command, userDetails);
    }

    private ApplicationTranslator<Endpoint, EndpointResource> translator() {
        return endpoint -> {
            //@formatter:off
            return EndpointResource.builder().endpointId(endpoint.getEndpointId().toString())
                    .endpointIdentifier(endpoint.getEndpointIdentifier().toString())
                    .endpointName(endpoint.getEndpointName()).build();
            //@formatter:on
        };
    }
}
