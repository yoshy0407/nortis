package org.nortis.domain.endpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nortis.domain.endpoint.value.TextType;

/**
 * テンプレートから作成されたメッセージです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public class RenderedMessage {

    private TextType textType;

    private String subject;

    private String body;

}
