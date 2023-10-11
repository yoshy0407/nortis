package org.nortis.port.rest.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * オペレーション権限のリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Getter
@Setter
public class OperationAuthorityResource {

    private String authorityId;

    private String displayName;

}
