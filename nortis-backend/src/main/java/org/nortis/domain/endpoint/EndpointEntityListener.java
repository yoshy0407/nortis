package org.nortis.domain.endpoint;

import lombok.Getter;
import lombok.Setter;
import org.nortis.domain.endpoint.event.EndpointDeletedEvent;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * エンドポイントのエンティティリスナーです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Component
public class EndpointEntityListener implements EntityListener<Endpoint> {

	/** イベント発行クラス */
	@Getter
	@Setter
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postDelete(Endpoint entity, PostDeleteContext<Endpoint> context) {
		applicationEventPublisher.publishEvent(
				new EndpointDeletedEvent(
						entity.getTenantId(), 
						entity.getEndpointId()));
	}

}
