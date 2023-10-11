package org.nortis.port.rest.payload;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.application.user.model.TenantRoleModel;

/**
 * ユーザ作成のリクエスト
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Setter
@Getter
public class UserCreateRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    private boolean adminFlg;

    private List<TenantRoleModel> roles;

}
