package org.nortis.port.rest.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.nortis.application.user.model.TenantRoleModel;

/**
 * ユーザロールに関するリクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserRoleRequest {

    private List<TenantRoleModel> roles;
}
