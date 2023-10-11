package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ロールオペレーションのリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@AllArgsConstructor
@Getter
@Setter
public class RoleOperationResource {

    private String roleId;

    private String operationId;

}
