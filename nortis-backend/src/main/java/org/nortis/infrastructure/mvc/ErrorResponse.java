package org.nortis.infrastructure.mvc;

import java.time.LocalDateTime;

/**
 * エラーレスポンスのオブジェクト
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param timestamp タイムスタンプ
 * @param code エラーコード
 * @param message エラーメッセージ
 */
public record ErrorResponse(LocalDateTime timestamp, String code, String message) {

}
