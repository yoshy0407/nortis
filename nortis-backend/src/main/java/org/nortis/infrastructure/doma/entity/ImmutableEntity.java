package org.nortis.infrastructure.doma.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.nortis.infrastructure.doma.EntityOperation;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Transient;

/**
 * イミュータブルなエンティティの抽象クラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Entity(immutable = true, metamodel = @Metamodel)
public class ImmutableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "CREATE_ID")
    private final String createId;

    @Column(name = "CREATE_DT")
    private final LocalDateTime createDt;

    @Transient
    private EntityOperation entityOperation = EntityOperation.UPDATE;

    /**
     * コンストラクター
     * 
     * @param createId 作成者
     * @param createDt 作成日時
     */
    protected ImmutableEntity(String createId, LocalDateTime createDt) {
        this.createId = createId;
        this.createDt = createDt;
    }

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
    public boolean isDelete() {
        return this.entityOperation.equals(EntityOperation.DELETE);
    }

}
