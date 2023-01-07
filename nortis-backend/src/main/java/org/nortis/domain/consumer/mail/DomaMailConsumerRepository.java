package org.nortis.domain.consumer.mail;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.value.ConsumerId;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.stereotype.Repository;

/**
 * Domaによる{@link MailConsumerRepository}の実装です
 * @author yoshiokahiroshi
 * @version
 */
@AllArgsConstructor
@Repository
public class DomaMailConsumerRepository implements MailConsumerRepository {

	/** コンフィグ */
	private final Config config;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<MailConsumer> get(ConsumerId consumerId) {
		MailConsumer_ consumer = new MailConsumer_();
		MailInfo_ info = new MailInfo_();
		Entityql entityql = new Entityql(config);
		return entityql
				.from(consumer)
				.innerJoin(info, on -> {
					on.eq(consumer.consumerId, info.consumerId);
				})
				.where(where -> where.eq(consumer.consumerId, consumerId))
				.associate(consumer, info, (e1, e2) -> {
					e1.getMailList().add(e2);
				})
				.fetchOptional();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MailConsumer> getFromEndpoint(EndpointId endpointId) {
		MailConsumer_ consumer = new MailConsumer_();
		MailInfo_ info = new MailInfo_();
		Entityql entityql = new Entityql(config);
		return entityql
				.from(consumer)
				.innerJoin(info, on -> {
					on.eq(consumer.consumerId, info.consumerId);
				})
				.where(where -> {
					where.eq(consumer.endpointId, endpointId);
				})
				.associate(consumer, info, (e1, e2) -> {
					e1.getMailList().add(e2);
				})
				.fetch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(MailConsumer mailConsumer) {
		Entityql entityql = new Entityql(config);
		
		MailConsumer_ consumer = new MailConsumer_();
		entityql.insert(consumer, mailConsumer).execute();

		MailInfo_ info = new MailInfo_();
		mailConsumer.getMailList().forEach(mailInfo -> {
			entityql.insert(info, mailInfo).execute();
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(MailConsumer mailConsumer) {
		Entityql entityql = new Entityql(config);
		
		MailInfo_ info = new MailInfo_();
		mailConsumer.getMailList().forEach(mailInfo -> {
			if (mailInfo.isAdd()) {
				entityql.insert(info, mailInfo).execute();
			}
			if (mailInfo.isRemove()) {
				entityql.delete(info, mailInfo).execute();
			}
		});
		
		MailConsumer_ consumer = new MailConsumer_();
		entityql.update(consumer, mailConsumer).execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(MailConsumer mailConsumer) {
		Entityql entityql = new Entityql(config);
		
		MailInfo_ info = new MailInfo_();
		mailConsumer.getMailList().forEach(mailInfo -> {
			entityql.delete(info, mailInfo).execute();
		});
		
		MailConsumer_ consumer = new MailConsumer_();
		entityql.delete(consumer, mailConsumer).execute();
	}

}
