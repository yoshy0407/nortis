package org.nortis.port.rest.resource;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * テナントロールのリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class TenantRoleResource {

    private String tenantId;

    private String roleId;

    private String roleName;

    private List<RoleOperationResource> operations;

}
