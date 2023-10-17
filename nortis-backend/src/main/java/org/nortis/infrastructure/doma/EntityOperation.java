package org.nortis.infrastructure.doma;

/**
 * エンティティの操作を表す列挙型です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public enum EntityOperation {
    /**
     * INSERTするオブジェクトであることを表す
     */
    INSERT,
    /**
     * UPDATEするオブジェクトであることを表す
     */
    UPDATE,
    /**
     * DELETEするオブジェクトであることを表す
     */
    DELETE;

}
