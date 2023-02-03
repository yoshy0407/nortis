package org.nortis.domain.user;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.user.value.UserId;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.stereotype.Repository;

/**
 * {@link SuserRepository}のDomaの実装です
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Repository
@AllArgsConstructor
public class DomaSuserRepository implements SuserRepository {

	private final Config config;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Suser> get(UserId userId) {
		Entityql entityql = new Entityql(config);
		Suser_ suser = new Suser_();
		TenantUser_ tenantUser = new TenantUser_();
		
		return entityql.from(suser)
				.leftJoin(tenantUser, on -> {
					on.eq(suser.userId, tenantUser.userId);
				})
				.associate(suser, tenantUser, (e1, e2) -> {
					e1.getTenantUserList().add(e2);
				})
				.where(where -> {
					where.eq(suser.userId, userId);
				})
				.fetchOptional();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Suser suser) {
		Entityql entityql = new Entityql(config);
		Suser_ user = new Suser_();
		TenantUser_ tenantUser = new TenantUser_();
		
		entityql.insert(user, suser).execute();
		suser.getTenantUserList().forEach(data -> {
			entityql.insert(tenantUser, data).execute();
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Suser suser) {
		Entityql entityql = new Entityql(config);
		Suser_ user = new Suser_();
		TenantUser_ tenantUser = new TenantUser_();

		entityql.update(user, suser).execute();
		suser.getTenantUserList().forEach(data -> {
			if (data.isInsert()) {
				entityql.insert(tenantUser, data).execute();
			} else if (data.isDeleted()) {
				entityql.delete(tenantUser, data).execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Suser suser) {
		Entityql entityql = new Entityql(config);
		Suser_ user = new Suser_();
		TenantUser_ tenantUser = new TenantUser_();
		
		suser.getTenantUserList().forEach(data -> {
			entityql.delete(tenantUser, data).execute();
		});
		entityql.delete(user, suser).execute();
	}

}
