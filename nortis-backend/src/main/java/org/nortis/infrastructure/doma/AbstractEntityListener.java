package org.nortis.infrastructure.doma;

import java.time.LocalDateTime;
import org.nortis.infrastructure.doma.entity.RootEntity;
import org.nortis.infrastructure.security.SecurityContextUtils;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/**
 * エンティティリスナーの抽象クラスです
 * 
 * @author yoshiokahiroshi
 * @param <E> エンティティ
 * @version 1.0.0
 */
public class AbstractEntityListener<E extends RootEntity> implements EntityListener<E> {

    @Override
    public void preInsert(E entity, PreInsertContext<E> context) {
        entity.setCreateDt(LocalDateTime.now());
        entity.setCreateId(SecurityContextUtils.getCurrentAuthorizedId().toString());
        entity.setVersion(Long.valueOf(1L));
    }

    @Override
    public void preUpdate(E entity, PreUpdateContext<E> context) {
        entity.setUpdateDt(LocalDateTime.now());
        entity.setUpdateId(SecurityContextUtils.getCurrentAuthorizedId().toString());
    }
}
