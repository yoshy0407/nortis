package org.nortis.domain.tenant;

import lombok.Getter;
import lombok.Setter;
import org.nortis.domain.tenant.event.TenantDeletedEvent;
import org.nortis.infrastructure.doma.AbstractEntityListener;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * テナントの{@link EntityListener}です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Component
public class TenantEntityListener extends AbstractEntityListener<Tenant> {

    /** イベント発行クラス */
    @Setter
    @Getter
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public void postDelete(Tenant entity, PostDeleteContext<Tenant> context) {
        TenantDeletedEvent event = new TenantDeletedEvent(entity.getTenantId(),
                SecurityContextUtils.getAuthentication().get());
        this.applicationEventPublisher.publishEvent(event);
    }

}
