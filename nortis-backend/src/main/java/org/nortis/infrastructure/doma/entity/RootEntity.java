package org.nortis.infrastructure.doma.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Version;

/**
 * 抽象的なエンティティクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Setter
@Entity
public class RootEntity {

    /**
     * 作成者
     */
    @Column(name = "CREATE_ID")
    private String createId;

    /**
     * 作成日付
     */
    @Column(name = "CREATE_DT")
    private LocalDateTime createDt;

    /**
     * 更新者
     */
    @Column(name = "UPDATE_ID")
    private String updateId;

    /**
     * 更新日付
     */
    @Column(name = "UPDATE_DT")
    private LocalDateTime updateDt;

    /**
     * バージョン
     */
    @Version
    @Column(name = "VERSION")
    private Long version;

}
