package org.nortis.application;

import java.util.Optional;

import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.MailConsumer;
import org.nortis.domain.mail.MailConsumerDomainService;
import org.nortis.domain.mail.MailConsumerRepository;
import org.nortis.domain.mail.value.ConsumerId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.domain.tenant.value.TenantId;
import org.nortis.infrastructure.annotation.ApplicationService;
import org.nortis.infrastructure.application.ApplicationTranslator;
import org.nortis.infrastructure.exception.DomainException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ApplicationService
public class MailConsumerApplicationService {

	private final MailConsumerRepository mailConsumerRepository;
	
	private final MailConsumerDomainService mailConsumerDomainService;
	
	public <R> R register(
			String inTenantId,
			String inEndpointId, 
			String inMailAddress, 
			String userId,
			ApplicationTranslator<MailConsumer, R> translator) {
		
		TenantId tenantId = TenantId.create(inTenantId);
		EndpointId endpointId = EndpointId.create(inEndpointId);
		MailAddress mailAddress = MailAddress.create(inMailAddress);
		
		MailConsumer mailConsumer = this.mailConsumerDomainService.createMailConsumer(tenantId, endpointId, mailAddress, userId);
		
		this.mailConsumerRepository.save(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	public <R> R changeMailAddress(
			String inConsumerId, 
			String inMailAddress,
			String userId,
			ApplicationTranslator<MailConsumer, R> translator) {
		ConsumerId consumerId = ConsumerId.create(inConsumerId);
		MailAddress mailAddress = MailAddress.create(inMailAddress);
		
		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException("");
		}
		
		MailConsumer mailConsumer = optMailConsumer.get();
		
		mailConsumer.updateMailAddress(mailAddress, userId);
		
		this.mailConsumerRepository.update(mailConsumer);
		
		return translator.translate(mailConsumer);
	}
	
	public void delete(String inConsumerId) {
		ConsumerId consumerId = ConsumerId.create(inConsumerId);
		
		Optional<MailConsumer> optMailConsumer = this.mailConsumerRepository.get(consumerId);
		
		if (optMailConsumer.isEmpty()) {
			throw new DomainException("MSG30002");
		}
		this.mailConsumerRepository.remove(optMailConsumer.get());
	}
	
}
