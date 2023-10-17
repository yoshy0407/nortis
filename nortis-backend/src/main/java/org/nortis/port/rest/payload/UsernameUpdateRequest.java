package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザ作成のリクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Setter
@Getter
public class UsernameUpdateRequest {

    @NotBlank
    private String username;

}
