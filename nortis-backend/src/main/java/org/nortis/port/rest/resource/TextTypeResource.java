package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * テキストタイプを表すリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@NoArgsConstructor
@Builder
@ToString
@AllArgsConstructor
@Getter
@Setter
public class TextTypeResource {

    private String code;

    private String displayName;

}
