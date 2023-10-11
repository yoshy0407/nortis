package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * コンシューマのリソースです
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
public class ConsumerResource {

    private String tenantId;

    private String consumerId;

    private String consumerName;

    private String consumerType;

    private String textType;

}
