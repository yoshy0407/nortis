package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザロールのリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserRoleResource {

    private String tenantId;

    private String roleId;
}
