package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * テナントロールのリクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Setter
@Getter
public class TenantRoleRequest {

    @NotBlank
    private String roleName;

    @NotEmpty
    List<String> authorities;
}
