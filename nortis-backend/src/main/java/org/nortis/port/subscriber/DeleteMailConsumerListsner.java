package org.nortis.port.subscriber;

import org.nortis.application.MailConsumerApplicationService;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class DeleteMailConsumerListsner {

	private final MailConsumerApplicationService mailConsumerApplicationService;
	
	public void subscribe(TenantDeletedEvent event) {
		
	}
}
