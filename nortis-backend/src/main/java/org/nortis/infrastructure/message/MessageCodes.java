package org.nortis.infrastructure.message;

/**
 * メッセージコードを保持するクラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class MessageCodes {

	private MessageCodes() {
		throw new IllegalStateException("このクラスはインスタンス化できません");
	}
	
	/**
	 * NORTIS00001
	 * @param labelName 項目名
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis00001(String labelName) {
		return new SimpleMessageCode("NORTIS00001", "%sが未設定です", new Object[] { labelName });
	}

	/**
	 * NORTIS00002
	 * @param labelName 項目名
	 * @param length 文字数
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis00002(String labelName, int length) {
		return new SimpleMessageCode("NORTIS00002", "%sは%s文字以内である必要があります", new Object[] { labelName, length });
	}

	/**
	 * NORTIS00003
	 * @param tableName テーブル名
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis00003(String tableName) {
		return new SimpleMessageCode("NORTIS00003", "指定された%s存在しません", new Object[] { tableName });
	}

	/**
	 * NORTIS00500
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis00500() {
		return new SimpleMessageCode("NORTIS00500", "想定外のエラーです", new Object[] { });
	}

	/**
	 * NORTIS10001
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis10001() {
		return new SimpleMessageCode("NORTIS10001", "指定されたテナントIDはすでに使われています", new Object[] {});
	}

	/**
	 * NORTIS10002
	 * @param tenantId テナントID
	 * @param result 結果
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis10002(String tenantId, String result) {
		return new SimpleMessageCode(
				"NORTIS10002", 
				"同じテナントIDのレコードが複数あります。テナントID: %s, 検索結果: %s", 
				new Object[] { tenantId, result });
	}

	/**
	 * NORTIS10003
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis10003() {
		return new SimpleMessageCode(
				"NORTIS10003", 
				"指定されたIDのテナントは存在しません", 
				new Object[] { });
	}
	
	/**
	 * NORTIS20001
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis20001() {
		return new SimpleMessageCode(
				"NORTIS20001", 
				"指定されたエンドポイントは存在しません", 
				new Object[] {});
	}

	/**
	 * NORTIS30001
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis30001() {
		return new SimpleMessageCode(
				"NORTIS30001", 
				"メールアドレスのフォーマットが不正です", new Object[] {});
	}

	/**
	 * NORTIS30002
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis30002() {
		return new SimpleMessageCode(
				"NORTIS30002", 
				"指定されたコンシューマIDのコンシューマが存在しません", 
				new Object[] { });
	}

	/**
	 * NORTIS30003
	 * @param mailAddress メールアドレス
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis30003(String mailAddress) {
		return new SimpleMessageCode(
				"NORTIS30003", 
				"指定されたメールアドレスはすでに登録されています。メールアドレス: %s", 
				new Object[] { mailAddress });
	}

	/**
	 * NORTIS30004
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis30004() {
		return new SimpleMessageCode(
				"NORTIS30004", 
				"メールアドレスは必ず一つ設定する必要があります", 
				new Object[] {  });
	}

	/**
	 * NORTIS30005
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis30005() {
		return new SimpleMessageCode(
				"NORTIS30005", 
				"指定されたメールアドレスコンシューマは存在しません", 
				new Object[] { });
	}

	/**
	 * NORTIS30006
	 * @param endpointId エンドポイントID
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis30006(String endpointId) {
		return new SimpleMessageCode(
				"NORTIS30006", 
				"エンドポイントID: %s", 
				new Object[] { endpointId });
	}

	/**
	 * NORTIS40001
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis40001() {
		return new SimpleMessageCode(
				"NORTIS40001", 
				"JSONのフォーマットが不正です", 
				new Object[] {});
	}

	/**
	 * NORTIS50001
	 * @param tenantId テナントID
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis50001(String tenantId) {
		return new SimpleMessageCode(
				"NORTIS50001", 
				"指定されたテナントへの権限はすでに存在します。テナントID: %s", 
				new Object[] { tenantId });
	}

	/**
	 * NORTIS50002
	 * @param tenantId テナントID
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis50002(String tenantId) {
		return new SimpleMessageCode(
				"NORTIS50002", 
				"指定されたテナントへの権限はすでにありません。テナントID: {0}", 
				new Object[] { tenantId });
	}

	/**
	 * NORTIS50003
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis50003() {
		return new SimpleMessageCode(
				"NORTIS50003", 
				"指定されたユーザIDのユーザはすでに存在します", 
				new Object[] { });
	}

	/**
	 * NORTIS50004
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis50004() {
		return new SimpleMessageCode(
				"NORTIS50004", 
				"指定されたユーザまたはパスワードは存在しません", 
				new Object[] { });
	}

	/**
	 * NORTIS50005
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis50005() {
		return new SimpleMessageCode(
				"NORTIS50005", 
				"権限がありません", 
				new Object[] { });
	}

	/**
	 * NORTIS60001
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis60001() {
		return new SimpleMessageCode(
				"NORTIS60001", 
				"認証エラーです", 
				new Object[] { });
	}

	/**
	 * NORTIS60002
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis60002() {
		return new SimpleMessageCode(
				"NORTIS60002", 
				"APIキーが期限切れです。再認証してください", 
				new Object[] { });
	}

	/**
	 * NORTIS90001
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis90001() {
		return new SimpleMessageCode(
				"NORTIS90001", 
				"メールのメッセージの作成に失敗しました", 
				new Object[] { });
	}

	/**
	 * NORTIS90002
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis90002() {
		return new SimpleMessageCode(
				"NORTIS90002", 
				"メールの送信に失敗しました", 
				new Object[] { });
	}

	/**
	 * NORTIS00003
	 * @return {@link MessageCode}
	 */
	public static MessageCode nortis90003() {
		return new SimpleMessageCode(
				"NORTIS90003", 
				"テンプレートの書き込みに失敗しました", 
				new Object[] { });
	}
	
}
