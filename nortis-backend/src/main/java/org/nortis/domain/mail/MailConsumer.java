package org.nortis.domain.mail;

import java.time.LocalDateTime;
import org.nortis.domain.endpoint.value.EndpointId;
import org.nortis.domain.mail.value.ConsumerId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

import lombok.Getter;
import lombok.Setter;

/**
 * メールコンシューマー
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Table(name = "MAIL_CONSUMER")
@Entity
public class MailConsumer {

	/**
	 * コンシューマID
	 */
	@Id
	@Column(name = "CONSUMER_ID")
	private ConsumerId consumerId;

	/**
	 * エンドポイントID
	 */
	@Column(name = "ENDPOINT_ID")
	private EndpointId endpointId;
	
	/**
	 * メールアドレス
	 */
	@Column(name = "MAIL_ADDRESS")
	private MailAddress mailAddress;

	/**
	 * 作成者ID
	 */
	@Column(name = "CREATE_ID")
	private String createId;
	
	/**
	 * 作成日付
	 */
	@Column(name = "CREATE_DT")
	private LocalDateTime createDt;

	/**
	 * 更新者ID
	 */
	@Setter
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	/**
	 * 更新日付
	 */
	@Setter
	@Column(name = "UPDATE_DT")
	private LocalDateTime updateDt;
	
	/**
	 * バージョン
	 */
	@Setter
	@Version
	@Column(name = "VERSION")
	private long version;
	
	/**
	 * メールアドレスを更新します
	 * @param mailAddress メールアドレス
	 * @param updateId 更新者ID
	 */
	public void updateMailAddress(MailAddress mailAddress, String updateId) {
		setMailAddress(mailAddress);
		setUpdateId(updateId);
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
	 * @param uuid エンドポイントID
	 */
	public void setEndpointId(EndpointId endppointId) {
		Validations.notNull(endppointId, "エンドポイントID");
		this.endpointId = endppointId;
	}

	/**
	 * メールアドレスを設定します
	 * @param mailAddress メールアドレス
	 */
	public void setMailAddress(MailAddress mailAddress) {
		Validations.notNull(mailAddress, "メールアドレス");
		this.mailAddress = mailAddress;
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
	 * @param no 番号
	 * @param mailAddress
	 * @param createId
	 * @return
	 */
	public static MailConsumer create(EndpointId endpointId, MailAddress mailAddress, String createId) {
		MailConsumer entity = new MailConsumer();
		entity.setConsumerId(ConsumerId.createNew());
		entity.setEndpointId(endpointId);
		entity.setMailAddress(mailAddress);
		entity.setCreateId(createId);
		entity.setCreateDt(LocalDateTime.now());
		entity.setVersion(1L);
		return entity;
	}
	
}
