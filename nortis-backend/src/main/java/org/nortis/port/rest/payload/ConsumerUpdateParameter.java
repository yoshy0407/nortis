package org.nortis.port.rest.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * コンシューマ作成リクエストのパラメータ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Getter
@Setter
public class ConsumerUpdateParameter {

    private String name;

    private String value;

}
