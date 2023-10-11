package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * テナント更新リクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Getter
@Setter
public class TenantUpdateRequest {

    @NotBlank
    private String tenantName;

}
