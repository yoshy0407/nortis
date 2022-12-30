package org.nortis.domain.endpoint;

import java.util.Optional;

import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.tenant.value.TenantId;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao
public interface EndpointRepository {

	@Select
	Optional<Endpoint> get(TenantId tenantId, EndpointId endpointId);
	
	@Insert
	int save(Endpoint endpoint);

	@Update
	int update(Endpoint endpoint);

	@Delete
	int remove(Endpoint endpoint);

}
