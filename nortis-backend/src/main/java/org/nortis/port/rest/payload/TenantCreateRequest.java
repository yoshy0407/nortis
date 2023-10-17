package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * テナント作成リクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Setter
@Getter
public class TenantCreateRequest {

    @NotBlank
    private String tenantIdentifier;

    @NotBlank
    private String tenantName;

}
