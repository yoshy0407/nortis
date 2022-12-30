package org.nortis.domain.tenant;

import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component
public class TenantEntityListener implements EntityListener<Tenant>{

	@Setter
	@Getter
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postDelete(Tenant entity, PostDeleteContext<Tenant> context) {
		this.applicationEventPublisher.publishEvent(new TenantDeletedEvent(entity.getTenantId()));
	}

	
}
