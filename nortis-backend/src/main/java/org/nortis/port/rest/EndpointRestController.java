package org.nortis.port.rest;

import lombok.AllArgsConstructor;
import org.nortis.application.endpoint.EndpointApplicationService;
import org.springframework.web.bind.annotation.RestController;

/**
 * エンドポイントのコントローラです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@RestController
@AllArgsConstructor
public class EndpointRestController {

    private final EndpointApplicationService endpointApplicationService;

}
