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

/**
 * メールコンシューマリポジトリ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ConfigAutowireable
@Dao
public interface MailConsumerRepository {

	/**
	 * １件検索します
	 * @param consumerId コンシューマID
	 * @return メールコンシューマ
	 */
	@Select
	Optional<MailConsumer> get(ConsumerId consumerId);

	/**
	 * エンドポイントIDからメールコンシューマを取得します
	 * @param endpointId エンドポイントID
	 * @return 複数件のメールコンシューマ
	 */
	@Select
	List<MailConsumer> getFromEndpointId(EndpointId endpointId);
	
	/**
	 * 保存します
	 * @param mailConsumer メールコンシューマ
	 * @return 保存件数
	 */
	@Insert
	int save(MailConsumer mailConsumer);

	/**
	 * 更新します
	 * @param mailConsumer メールコンシューマ
	 * @return 更新件数
	 */
	@Update
	int update(MailConsumer mailConsumer);

	/**
	 * 削除します
	 * @param mailConsumer メールコンシューマ
	 * @return 削除件数
	 */
	@Delete
	int remove(MailConsumer mailConsumer);

}
