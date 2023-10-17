package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * メッセージテンプレートのリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Builder
@ToString
@AllArgsConstructor
@Getter
@Setter
public class MessageTemplateResource {

    private String endpointId;

    private String textType;

    private String subjectTemplate;

    private String bodyTemplate;
}
