package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * エンドポイントのリソースです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@AllArgsConstructor
@Getter
@Setter
public class EndpointResource {

    private String tenantId;

    private String endpointId;

    private String endpointIdentifier;

    private String endpointName;

}
