package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * テナントロールの権限を更新します
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Setter
@Getter
public class TenantRoleUpdateAuthorityRequest {

    @NotEmpty
    private List<String> grantAuthorities;

    @NotEmpty
    private List<String> revokeAuthorities;

}
