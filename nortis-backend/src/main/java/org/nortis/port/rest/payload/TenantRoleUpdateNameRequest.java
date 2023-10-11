package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * テナントロール名更新リクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Setter
@Getter
public class TenantRoleUpdateNameRequest {

    @NotBlank
    private String roleName;

}
