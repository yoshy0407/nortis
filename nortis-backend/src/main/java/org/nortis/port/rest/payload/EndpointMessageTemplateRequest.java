package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * エンドポイントのメッセージテンプレートのリクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class EndpointMessageTemplateRequest {

    @NotBlank
    private String subjectTemplate;

    @NotBlank
    private String bodyTemplate;

}
