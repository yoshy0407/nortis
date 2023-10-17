package org.nortis.port.rest.payload;

import lombok.Builder;

/**
 * ログアウトのリクエストオブジェクト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 * @param loginId ログインID
 */
@Builder
public record LogoutRequest(String loginId) {

}
