package org.nortis.infrastructure.doma.entity;

import lombok.Getter;
import org.nortis.infrastructure.doma.EntityOperation;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;

/**
 * 抽象的なエンティティ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Entity
@Getter
public class AbstractEntity extends RootEntity {

    @Transient
    private EntityOperation entityOperation = EntityOperation.UPDATE;

    /**
     * 削除するエンティティとマークします
     */
    public void setDelete() {
        this.entityOperation = EntityOperation.DELETE;
    }

    /**
     * 登録するエンティティとマークします
     */
    public void setInsert() {
        this.entityOperation = EntityOperation.INSERT;
    }

    @SuppressWarnings("javadoc")
    public boolean isInsert() {
        return this.entityOperation.equals(EntityOperation.INSERT);
    }

    @SuppressWarnings("javadoc")
    public boolean isUpdate() {
        return this.entityOperation.equals(EntityOperation.UPDATE);
    }

    @SuppressWarnings("javadoc")
    public boolean isDelete() {
        return this.entityOperation.equals(EntityOperation.DELETE);
    }

}
