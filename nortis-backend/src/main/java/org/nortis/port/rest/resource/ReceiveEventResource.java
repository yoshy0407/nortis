package org.nortis.port.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 受信イベントのリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ReceiveEventResource {

    private String eventId;

    private String occuredOn;

}
