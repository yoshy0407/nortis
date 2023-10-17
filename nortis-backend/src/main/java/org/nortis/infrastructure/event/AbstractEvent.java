package org.nortis.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;

/**
 * 抽象的なイベントです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public class AbstractEvent {

    private final Authentication authentication;

}
