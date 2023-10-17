package org.nortis.port.rest.resource;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ユーザのリソース
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UserResource {

    private String userId;

    private String username;

    private String loginId;

    private boolean adminFlg;

    private List<UserRoleResource> roles;

}
