package org.nortis.port.rest.payload;

import lombok.Builder;

/**
 * ログインのリクエストオブジェクト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param loginId  ログインID
 * @param password パスワード
 */
@Builder
public record LoginRequest(String loginId, String password) {

}
