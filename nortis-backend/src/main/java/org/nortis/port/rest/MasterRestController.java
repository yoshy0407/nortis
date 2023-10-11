package org.nortis.port.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.nortis.application.tenant.TenantRoleApplicationService;
import org.nortis.domain.endpoint.value.TextType;
import org.nortis.domain.tenant.OperationAuthority;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.port.rest.resource.OperationAuthorityResource;
import org.nortis.port.rest.resource.TextTypeResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * マスターに関するコントローラーです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RequestMapping("/master")
@RestController
public class MasterRestController {

    private final TenantRoleApplicationService tenantRoleApplicationService;

    private final EndpointApplicationService endpointApplicationService;

    /**
     * オペレーション権限の全件を返却します
     * 
     * @return オペレーション権限の全件
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("operation-authority")
    public List<OperationAuthorityResource> getOperationAuthority() {
        return this.tenantRoleApplicationService.getOperationAuthorityList(operationAuthorityTranslator());
    }

    /**
     * 全てのテキストタイプを取得します
     * 
     * @return テキストタイプのリスト
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("text-type")
    public List<TextTypeResource> getTextType() {
        return this.endpointApplicationService.getTextTypes(textTypeTranslator());
    }

    private ApplicationTranslator<TextType, TextTypeResource> textTypeTranslator() {
        return textType -> {
            return TextTypeResource.builder().code(textType.getTextType()).displayName(textType.getTextTypeName())
                    .build();
        };
    }

    private ApplicationTranslator<OperationAuthority, OperationAuthorityResource> operationAuthorityTranslator() {
        return operationAuthority -> {
            //@formatter:off
            return OperationAuthorityResource.builder()
                    .authorityId(operationAuthority.getAuthorityId())
                    .displayName(operationAuthority.getDisplayName())
                    .build();
            //@formatter:on
        };
    }

}
