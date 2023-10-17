package org.nortis.application.endpoint;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointIdentifier;
import org.nortis.domain.event.ReceiveEvent;
import org.nortis.domain.event.ReceiveEventRepository;
import org.nortis.domain.service.AuthorityCheckDomainService;
import org.nortis.domain.service.EndpointDomainService;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantIdentifier;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.message.MessageCodes;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.transaction.annotation.Transactional;

/**
 * エンドポイントの受信に関するサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
@ApplicationService
public class EndpointReceiveApplicationService {

    private final TenantRepository tenantRepository;

    private final EndpointRepository endpointRepository;

    private final ReceiveEventRepository receiveEventRepository;

    private final AuthorityCheckDomainService authorityCheckDomainService;

    private final EndpointDomainService endpointDomainService;

    /**
     * エンドポイントの受信処理です
     * 
     * @param <R>                   変換後のクラス
     * @param rawTenantIdentifier   テナント識別子
     * @param rawEndpointIdentifier エンドポイント識別子
     * @param parameters            パラメータ
     * @param userDetails           ユーザ
     * @param translator            変換処理
     * @return 変換後の値
     * @throws DomainException ビジネスロジックエラー
     */
    public <R> R receive(String rawTenantIdentifier, String rawEndpointIdentifier, Map<String, Object> parameters,
            NortisUserDetails userDetails, ApplicationTranslator<ReceiveEvent, R> translator) throws DomainException {

        TenantIdentifier tenantIdentifier = TenantIdentifier.create(rawTenantIdentifier);
        Optional<Tenant> optTenant = this.tenantRepository.getByTenantIdentifier(tenantIdentifier);
        if (optTenant.isEmpty()) {
            throw new DomainException(MessageCodes.nortis10003());
        }

        this.authorityCheckDomainService.checkHasWriteEndpoint(userDetails, optTenant.get());

        EndpointIdentifier endpointIdentifier = EndpointIdentifier.create(rawEndpointIdentifier);
        Optional<Endpoint> optEndpoint = this.endpointRepository.getByEndpointIdentifier(optTenant.get().getTenantId(),
                endpointIdentifier);
        if (optEndpoint.isEmpty()) {
            throw new DomainException(MessageCodes.nortis20001());
        }

        ReceiveEvent receiveEvent = this.endpointDomainService.receiveEndpoint(optEndpoint.get(), parameters);

        this.receiveEventRepository.save(receiveEvent);

        return translator.translate(receiveEvent);
    }

}
