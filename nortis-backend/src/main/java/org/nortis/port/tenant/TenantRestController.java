package org.nortis.port.tenant;

import lombok.AllArgsConstructor;
import org.nortis.application.tenant.TenantApplicationService;
import org.nortis.application.tenant.TenantNameUpdateCommand;
import org.nortis.application.tenant.TenantRegisterCommand;
import org.nortis.infrastructure.security.user.NortisUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * テナントのエンドポイントのコントローラです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@RequestMapping("/tenant")
@RestController
public class TenantRestController {

	private final TenantApplicationService tenantApplicationService;
	
	/**
	 * テナント情報を返却します
	 * @param tenantId テナントID
	 * @param userDetails 認証済みユーザ
	 * @return テナントリソース
	 */
	@GetMapping("{tenantId}")
	public TenantResource get(
			@PathVariable(name = "tenantId", required = true) String tenantId,
			@AuthenticationPrincipal NortisUserDetails userDetails) {
		//:TODO
		return null;
	}
	
	/**
	 * テナントを作成します
	 * @param req リクエストボディ
	 * @param userDetails 認証済みユーザ
	 * @return 作成したテナントリソース
	 */
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping
	public TenantResource post(
			@RequestBody TenantRequest req, 
			@AuthenticationPrincipal NortisUserDetails userDetails) {
		TenantRegisterCommand command = new TenantRegisterCommand(req.tenantId(), req.tenantName(), userDetails.getUsername());
		return this.tenantApplicationService.register(command, data -> {
			return new TenantResource(data.getTenantId().toString(), data.getTenantName());
		});
	}
	
	/**
	 * テナントを更新します
	 * @param tenantId テナントID
	 * @param req リクエストボディ
	 * @return 変更したテナントリソース
	 */
	@PatchMapping("{tenantId}")
	public TenantResource patch(
			@PathVariable(name = "tenantId", required = true) String tenantId,
			@RequestBody TenantPatchRequest req) {
		TenantNameUpdateCommand command = new TenantNameUpdateCommand(tenantId, req.name(), null);
		return this.tenantApplicationService.changeName(command, data -> {
			return new TenantResource(data.getTenantId().toString(), data.getTenantName());
		});
	}

	/**
	 * テナントを削除します
	 * @param tenantId テナントID
	 */
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping
	public void delete(@PathVariable(name = "tenantId", required = true) String tenantId) {
		this.tenantApplicationService.delete(tenantId, null);
	}
	
}
