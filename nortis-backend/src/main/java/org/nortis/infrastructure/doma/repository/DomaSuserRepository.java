package org.nortis.infrastructure.doma.repository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.tenant.value.RoleId;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.Suser_;
import org.nortis.domain.user.UserRole;
import org.nortis.domain.user.UserRole_;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.nortis.infrastructure.application.Paging;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStarting;
import org.springframework.stereotype.Repository;

/**
 * {@link SuserRepository}のDomaの実装です
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AllArgsConstructor
@Repository
public class DomaSuserRepository implements SuserRepository {

    private final Entityql entityql;

    private final Suser_ suser = new Suser_();

    private final UserRole_ userRole = new UserRole_();

    @Override
    public Optional<Suser> getByUserId(UserId userId) {
        //@formatter:off
        return select()
                .where(c -> c.eq(this.suser.userId, userId))
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public Optional<Suser> getByLoginId(LoginId loginId) {
        //@formatter:off
        return select()
                .where(c -> c.eq(this.suser.loginId, loginId))
                .fetchOptional();
        //@formatter:on
    }

    @Override
    public void save(Suser suser) {
        entityql.insert(this.suser, suser).execute();
        entityql.insert(this.userRole, suser.getUserRoles()).execute();
    }

    @Override
    public List<Suser> getFromRoleId(RoleId roleId) {
        //@formatter:off
        return select()
                .where(c -> c.eq(this.userRole.roleId, roleId))
                .orderBy(orderBy -> orderBy.asc(this.suser.userId))
                .fetch();
        //@formatter:on
    }

    @Override
    public List<Suser> getListPaging(Paging paging) {
        //@formatter:off
        return this.entityql.from(this.suser)
                .leftJoin(this.userRole, on ->  on.eq(this.suser.userId, this.userRole.userId))
                .associate(this.suser, this.userRole, (e1, e2) -> {
                    e1.getUserRoles().add(e2);
                })
                .offset(paging.offset())
                .limit(paging.limit())
                .orderBy(o -> o.asc(this.suser.updateId))
                .fetch();
        //@formatter:on                
    }

    @Override
    public void update(Suser suser) {
        entityql.update(this.suser, suser).execute();
        for (UserRole userRole : suser.getUserRoles()) {
            if (userRole.isDelete()) {
                entityql.delete(this.userRole, userRole).execute();
            }
            if (userRole.isInsert()) {
                entityql.insert(this.userRole, userRole).execute();
            }
        }
    }

    @Override
    public void remove(Suser suser) {
        entityql.delete(this.userRole, suser.getUserRoles()).execute();
        entityql.delete(this.suser, suser).execute();
    }

    private EntityqlSelectStarting<Suser> select() {
        //@formatter:off
        return entityql
                .from(this.suser)
                .leftJoin(this.userRole, on ->  on.eq(this.suser.userId, this.userRole.userId))
                .associate(this.suser, this.userRole, (e1, e2) -> {
                    e1.getUserRoles().add(e2);
                });
        //@formatter:on

    }

}
