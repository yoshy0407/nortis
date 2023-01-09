package org.nortis.application.event;

/**
 * 受信イベントを登録するコマンド
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param tenantId テナントID
 * @param endpointId エンドポイントID
 * @param paramaterJson パラメータのJSON
 */
public record ReceiveEventRegisterCommand(
		String tenantId,
		String endpointId,
		String paramaterJson) {

}
