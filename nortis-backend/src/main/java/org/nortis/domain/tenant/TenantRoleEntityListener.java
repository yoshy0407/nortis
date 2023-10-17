package org.nortis.domain.tenant;

import lombok.Getter;
import lombok.Setter;
import org.nortis.domain.tenant.event.TenantRoleDeletedEvent;
import org.nortis.infrastructure.doma.AbstractEntityListener;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * テナントロールのエンティティリスナーです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Component
public class TenantRoleEntityListener extends AbstractEntityListener<TenantRole> {

    /** イベント発行クラス */
    @Setter
    @Getter
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void postDelete(TenantRole entity, PostDeleteContext<TenantRole> context) {
        TenantRoleDeletedEvent event = new TenantRoleDeletedEvent(entity.getTenantId().toString(),
                entity.getRoleId().toString(), SecurityContextUtils.getAuthentication().get());
        this.applicationEventPublisher.publishEvent(event);
    }

}
