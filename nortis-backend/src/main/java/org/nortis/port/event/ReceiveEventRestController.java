package org.nortis.port.event;

import lombok.AllArgsConstructor;
import org.nortis.application.event.ReceiveEventApplicationService;
import org.nortis.application.event.ReceiveEventRegisterCommand;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * イベントのエンドポイントのコントローラ
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RequestMapping("/notice")
@RestController
public class ReceiveEventRestController {

	private final ReceiveEventApplicationService receiveEventApplicationService;
	
	/**
	 * 通知イベントを登録します
	 * @param endpointId エンドポイントID
	 * @param jsonBody パラメータのJSON
	 * @param user 認証済みユーザ
	 * @return 登録情報レスポンス
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/{endpointId}")
	public EventRegisterResponse register(
			@PathVariable("endpointId") String endpointId,
			@RequestBody String jsonBody,
			@AuthenticationPrincipal NortisUserDetails user) {
		//:TODO ここで
		ReceiveEventRegisterCommand command = 
				new ReceiveEventRegisterCommand(user.getTenantId()[0], endpointId, jsonBody);
		return this.receiveEventApplicationService.register(command, event -> {
			return new EventRegisterResponse(event.getEventId().toString(), event.getOccuredOn());
		});
	}
}
