package org.nortis.port.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * イベント登録後のレスポンスオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 2.0.0
 * @param eventId イベントID
 * @param registerDatetime 登録日付 
 */
public record EventRegisterResponse(
		String eventId,
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime registerDatetime) {

}
