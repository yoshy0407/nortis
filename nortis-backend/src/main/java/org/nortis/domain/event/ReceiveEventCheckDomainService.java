package org.nortis.domain.event;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.Endpoint;
import org.nortis.domain.endpoint.EndpointRepository;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.Tenant;
import org.nortis.domain.tenant.TenantRepository;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.DomainService;
import org.nortis.infrastructure.exception.DomainException;


/**
 * 受信イベントのチェックに関するドメインサービスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@DomainService
public class ReceiveEventCheckDomainService {

	private final TenantRepository tenantRepository;
	
	private final EndpointRepository endpointRepository;

	/**
	 * 登録前のチェック処理を実施します
	 * @param tenantId テナントID
	 * @param endpointId エンドポイントID
	 */
	public void checkBeforeRegister(TenantId tenantId, EndpointId endpointId) {
		Optional<Tenant> tenant = this.tenantRepository.get(tenantId);
		if (tenant.isEmpty()) {
			throw new DomainException("MSG10003");
		}
		Optional<Endpoint> endpoint = this.endpointRepository.get(tenantId, endpointId);
		if (endpoint.isEmpty()) {
			throw new DomainException("MSG00003", "エンドポイント");			
		}
	}

}
