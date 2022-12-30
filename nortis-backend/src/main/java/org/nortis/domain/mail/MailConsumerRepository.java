package org.nortis.domain.mail;

import java.util.List;
import java.util.Optional;

import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.value.ConsumerId;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao
public interface MailConsumerRepository {

	@Select
	Optional<MailConsumer> get(ConsumerId consumerId);

	@Select
	List<MailConsumer> getFromEndpointId(EndpointId endpointId);
	
	@Insert
	int save(MailConsumer mailConsumer);

	@Update
	int update(MailConsumer mailConsumer);

	@Delete
	int remove(MailConsumer mailConsumer);

}
