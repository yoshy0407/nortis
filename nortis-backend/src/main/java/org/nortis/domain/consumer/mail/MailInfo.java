package org.nortis.domain.consumer.mail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nortis.domain.mail.value.ConsumerId;
import org.nortis.domain.mail.value.MailAddress;
import org.nortis.infrastructure.validation.Validations;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

/**
 * メール情報
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@ToString
@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "MAIL_INFO")
public class MailInfo {
	
	/**
	 * コンシューマID
	 */
	@Id
	@Column(name = "CONSUMER_ID")
	private final ConsumerId consumerId;

	/**
	 * 番号
	 */
	@Id
	@Column(name = "ORDER_NO")
	private final int orderNo;
	
	/**
	 * メールアドレス
	 */
	@Column(name = "MAIL_ADDRESS")
	private final MailAddress mailAddress;

	/**
	 * 追加フラグ
	 */
	@Transient
	@Getter
	@Setter
	private boolean add = false;
	
	/**
	 * 削除フラグ
	 */
	@Transient
	@Getter
	@Setter
	private boolean remove = false;
	
	/**
	 * インスタンスを生成します
	 * @param consumerId コンシューマID
	 * @param orderNo 番号
	 * @param mailAddress メールアドレス
	 */
	public MailInfo(
			ConsumerId consumerId,
			int orderNo,
			MailAddress mailAddress) {
		Validations.notNull(consumerId, "コンシューマID");
		this.consumerId = consumerId;
		this.orderNo = orderNo;
		Validations.notNull(consumerId, "コンシューマID");
		this.mailAddress = mailAddress;		
	}

}
