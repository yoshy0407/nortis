package org.nortis.infrastructure.doma;

import org.nortis.infrastructure.doma.entity.RootEntity;
import org.seasar.doma.jdbc.entity.EntityListener;

/**
 * メタデータを更新する{@link EntityListener}です
 * 
 * @author yoshiokahiroshi
 * @version 2.0.0
 * @param <E> エンティティクラス
 */
public class NortisEntityListener<E extends RootEntity> extends AbstractEntityListener<E> {

}
