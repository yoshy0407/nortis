package org.nortis.domain.consumer.mail;

import java.util.List;
import java.util.Optional;
import org.nortis.domain.consumer.mail.value.ConsumerId;
import org.nortis.domain.endpoint.value.EndpointId;

/**
 * メールコンシューマのリポジトリです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public interface MailConsumerRepository {

	/**
	 * コンシューマIDからオブジェクトを取得します
	 * @param consumerId コンシューマID
	 * @return メールコンシューマ
	 */
	Optional<MailConsumer> get(ConsumerId consumerId);
	
	/**
	 * エンドポイントIDからオブジェクトを取得します
	 * @param endpointId エンドポイントID
	 * @return メールコンシューマ
	 */
	List<MailConsumer> getFromEndpoint(EndpointId endpointId);
	
	/**
	 * メールコンシューマを保存します
	 * @param mailConsumer メールコンシューマ
	 */
	void save(MailConsumer mailConsumer);
	
	/**
	 * メールコンシューマを更新します
	 * @param mailConsumer メールコンシューマ
	 */
	void update(MailConsumer mailConsumer);

	/**
	 * メールコンシューマを削除します
	 * @param mailConsumer メールコンシューマ
	 */
	void remove(MailConsumer mailConsumer);
	
}
