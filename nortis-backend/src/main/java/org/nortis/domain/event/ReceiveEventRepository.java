package org.nortis.domain.event;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao
public interface ReceiveEventRepository {


	@Select
	List<ReceiveEvent> notSubscribed();
	
	@Insert
	int save(ReceiveEvent receiveEvent);
	
	@Update
	int update(ReceiveEvent receiveEvent);
	
	@Delete
	int remove(ReceiveEvent receiveEvent);

}
