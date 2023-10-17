package org.nortis.port.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * フロントエンドのエラー時の情報をバックエンドのログに出力するコントローラです
 * バックエンドと疎通ができない場合、ログを取得できないがそれ以外の場合、バックエンド側でログを出力できるようにしておくため
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Slf4j
@RestController
public class FrontendErrorLogController {

    /**
     * エラーログを出力します
     * 
     * @param errorInfo エラー情報
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/frontend/error")
    public void errorLog(@RequestBody String errorInfo) {
        log.error("frontend error: {}", errorInfo);
    }
}
