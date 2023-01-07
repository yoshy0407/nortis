package org.nortis.domain.consumer.mail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.consumer.mail.value.ConsumerId;
import org.nortis.domain.consumer.mail.value.MailAddress;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.infrastructure.exception.DomainException;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

/**
 * メールコンシューマー
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ToString
@Table(name = "MAIL_CONSUMER")
@Entity(metamodel = @Metamodel)
public class MailConsumer {

	/**
	 * コンシューマID
	 */
	@Getter
	@Id
	@Column(name = "CONSUMER_ID")
	private ConsumerId consumerId;

	/**
	 * エンドポイントID
	 */
	@Getter
	@Column(name = "ENDPOINT_ID")
	private EndpointId endpointId;
	
	/**
	 * メールアドレスのリスト
	 */
	@Getter
	@Transient
	private final List<MailInfo> mailList = new ArrayList<>();
	
	/**
	 * 作成者ID
	 */
	@Getter
	@Column(name = "CREATE_ID")
	private String createId;
	
	/**
	 * 作成日付
	 */
	@Getter
	@Column(name = "CREATE_DT")
	private LocalDateTime createDt;

	/**
	 * 更新者ID
	 */
	@Getter
	@Setter
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	/**
	 * 更新日付
	 */
	@Getter
	@Setter
	@Column(name = "UPDATE_DT")
	private LocalDateTime updateDt;
	
	/**
	 * バージョン
	 */
	@Setter
	@Getter
	@Version
	@Column(name = "VERSION")
	private long version;
	
	/**
	 * メールアドレスを追加します
	 * @param address メールアドレス
	 */
	public void addMailAddress(MailAddress address) {
		for (MailInfo mailInfo : this.mailList) {
			if (mailInfo.getMailAddress().equals(address)) {
				throw new DomainException("MSG30003", address.toString());
			}
		}
		MailInfo mailInfo = new MailInfo(this.consumerId, this.mailList.size() + 1, address);
		mailInfo.setAdd(true);
		this.mailList.add(mailInfo);
	}
	
	/**
	 * メールアドレスを削除します
	 * @param mailAddress メールアドレス
	 */
	public void removeMailAddress(MailAddress mailAddress) {
		for (MailInfo mailInfo : this.mailList) {
			if (mailInfo.getMailAddress().equals(mailAddress)) {
				mailInfo.setRemove(true);
			}
		}
	}
	
	/**
	 * エンドポイントIDをリセットします
	 * @param userId 更新者ID
	 */
	public void resetEndpointId(String userId) {
		setEndpointId(null);
		setUpdateId(userId);
		setUpdateDt(LocalDateTime.now());
	}
	
	/**
	 * コンシューマIDを設定します
	 * @param consumerId コンシューマID
	 */
	public void setConsumerId(ConsumerId consumerId) {
		Validations.notNull(consumerId, "コンシューマID");
		this.consumerId = consumerId;
	}
	
	/**
	 * エンドポイントIDを設定します
	 * @param endppointId エンドポイントID
	 */
	public void setEndpointId(EndpointId endppointId) {
		this.endpointId = endppointId;
	}

	/**
	 *  作成者IDを設定します
	 * @param createId 作成者ID
	 */
	public void setCreateId(String createId) {
		Validations.hasText(createId, "作成者ID");
		this.createId = createId;
	}

	/**
	 * 作成日付を設定します
	 * @param createDt 作成日付
	 */
	public void setCreateDt(LocalDateTime createDt) {
		Validations.notNull(createDt, "作成日付");
		this.createDt = createDt;
	}

	/**
	 * 作成します
	 * @param endpointId エンドポイントID
	 * @param addressList 登録するアドレス
	 * @param createId 作成者
	 * @return 作成したオブジェクト
	 */
	public static MailConsumer create(EndpointId endpointId, List<MailAddress> addressList, String createId) {
		MailConsumer entity = new MailConsumer();
		ConsumerId consumerId = ConsumerId.createNew();
		entity.setConsumerId(consumerId);
		entity.setEndpointId(endpointId);
		if (addressList == null || addressList.isEmpty()) {
			throw new DomainException("MSG30004");
		}
		for (MailAddress address : addressList) {
			entity.addMailAddress(address);
		}
		entity.setCreateId(createId);
		entity.setCreateDt(LocalDateTime.now());
		entity.setVersion(1L);
		return entity;
	}
	
}
