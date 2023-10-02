package org.nortis.infrastructure.doma.repository;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.nortis.domain.user.Suser;
import org.nortis.domain.user.SuserRepository;
import org.nortis.domain.user.Suser_;
import org.nortis.domain.user.UserRole;
import org.nortis.domain.user.UserRole_;
import org.nortis.domain.user.value.LoginId;
import org.nortis.domain.user.value.UserId;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
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
        return selectInternal(c -> c.eq(this.suser.userId, userId));
    }

    @Override
    public Optional<Suser> getByLoginId(LoginId loginId) {
        return selectInternal(c -> c.eq(this.suser.loginId, loginId));
    }

    @Override
    public void save(Suser suser) {
        entityql.insert(this.suser, suser).execute();
        entityql.insert(this.userRole, suser.getUserRoles()).execute();
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

    private Optional<Suser> selectInternal(Consumer<WhereDeclaration> condition) {
        //@formatter:off
        return entityql
                .from(this.suser)
                .leftJoin(this.userRole, on ->  on.eq(this.suser.userId, this.userRole.userId))
                .where(condition)                
                .associate(this.suser, this.userRole, (e1, e2) -> {
                    e1.getUserRoles().add(e2);
                })
                .fetchOptional();
        //@formatter:on

    }
}
