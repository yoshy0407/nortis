package org.nortis.domain.endpoint;

import java.time.LocalDateTime;
import java.util.UUID;

import org.nortis.domain.endpoint.value.MailAddress;
import org.nortis.infrastructure.validation.Validations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
	 * UUID
	 */
	@Id
	@Column(name = "UUID")
	private UUID uuid;
	
	/**
	 * No
	 */
	@Id
	@Column(name = "NO")
	private int no;
	
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
	@Column(name = "UPDATE_ID")
	private String updateId;
	
	/**
	 * 更新日付
	 */
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
	 * UUIDを設定します
	 * @param uuid UUID
	 */
	public void setUuid(UUID uuid) {
		Validations.notNull(uuid, "UUID");
		this.uuid = uuid;
	}

	/**
	 * Noを設定します
	 * @param no No
	 */
	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * メールアドレスを設定します
	 * @param mailAddress メールアドレス
	 */
	public void setMailAddress(MailAddress mailAddress) {
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
	 * 更新者IDを設定します
	 * @param updateId 更新者ID
	 */
	public void setUpdateId(String updateId) {
		Validations.hasText(updateId, "更新者ID");
		this.updateId = updateId;
	}

	/**
	 * 更新日付を設定します
	 * @param updateDt 更新日付
	 */
	public void setUpdateDt(LocalDateTime updateDt) {
		Validations.notNull(updateDt, "更新日付");
		this.updateDt = updateDt;
	}
	
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

}
