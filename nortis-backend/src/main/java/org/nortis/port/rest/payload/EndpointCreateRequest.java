package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * エンドポイントの登録リクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@Setter
@Getter
public class EndpointCreateRequest {

    @NotBlank
    private String endpointIdentifier;

    @NotBlank
    private String endpointName;

    @NotBlank
    private String textType;

    @NotBlank
    private String subjectTemplate;

    @NotBlank
    private String bodyTemplate;

}
