package org.nortis.infrastructure.message;

/**
 * メッセージコードを保持するクラスです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class MessageCodes {

    private MessageCodes() {
        throw new IllegalStateException("このクラスはインスタンス化できません");
    }

    /**
     * NORTIS00001
     * 
     * @param labelName 項目名
     * @return {@link MessageCode}
     */
    public static MessageCode nortis00001(String labelName) {
        return new SimpleMessageCode("NORTIS00001", "%sが未設定です", new Object[] { labelName });
    }

    /**
     * NORTIS00002
     * 
     * @param labelName 項目名
     * @param length    文字数
     * @return {@link MessageCode}
     */
    public static MessageCode nortis00002(String labelName, int length) {
        return new SimpleMessageCode("NORTIS00002", "%sは%s文字以内である必要があります", new Object[] { labelName, length });
    }

    /**
     * NORTIS00003
     * 
     * @param tableName テーブル名
     * @return {@link MessageCode}
     */
    public static MessageCode nortis00003(String tableName) {
        return new SimpleMessageCode("NORTIS00003", "指定された%s存在しません", new Object[] { tableName });
    }

    /**
     * NORTIS00500
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis00500() {
        return new SimpleMessageCode("NORTIS00500", "想定外のエラーです", new Object[] {});
    }

    /**
     * NORTIS10001
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis10001() {
        return new SimpleMessageCode("NORTIS10001", "指定されたテナント識別子はすでに使われています", new Object[] {});
    }

    /**
     * NORTIS10002
     * 
     * @param tenantId テナントID
     * @param result   結果
     * @return {@link MessageCode}
     */
    @Deprecated
    public static MessageCode nortis10002(String tenantId, String result) {
        return new SimpleMessageCode("NORTIS10002", "同じテナントIDのレコードが複数あります。テナントID: %s, 検索結果: %s",
                new Object[] { tenantId, result });
    }

    /**
     * NORTIS10003
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis10003() {
        return new SimpleMessageCode("NORTIS10003", "指定されたIDのテナントは存在しません", new Object[] {});
    }

    /**
     * NORTIS10004
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis10004() {
        return new SimpleMessageCode("NORTIS10004", "採番しているテナントIDが重複しています", new Object[] {});
    }

    /**
     * NORTIS10005
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis10005() {
        return new SimpleMessageCode("NORTIS10005", "指定されたIDのロールは存在しません", new Object[] {});
    }

    /**
     * NORTIS20001
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis20001() {
        return new SimpleMessageCode("NORTIS20001", "指定されたエンドポイントは存在しません", new Object[] {});
    }

    /**
     * NORTIS20002
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis20002() {
        return new SimpleMessageCode("NORTIS20002", "指定されたテキストパターンのテンプレートはすでに存在します", new Object[] {});
    }

    /**
     * NORTIS20003
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis20003() {
        return new SimpleMessageCode("NORTIS20003", "指定されたテキストパターンのテンプレートは存在しません", new Object[] {});
    }

    /**
     * NORTIS30001
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis30001() {
        return new SimpleMessageCode("NORTIS30001", "メールアドレスのフォーマットが不正です", new Object[] {});
    }

    /**
     * NORTIS30002
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis30002() {
        return new SimpleMessageCode("NORTIS30002", "指定されたコンシューマIDのコンシューマが存在しません", new Object[] {});
    }

    /**
     * NORTIS30003
     * 
     * @param parameterDisplayName 画面表示名
     * @return {@link MessageCode}
     */
    public static MessageCode nortis30003(String parameterDisplayName) {
        return new SimpleMessageCode("NORTIS30003", "%sは必須のコンシューマパラメータです", new Object[] { parameterDisplayName });
    }

    /**
     * NORTIS30004
     * 
     * @param errorMessage エラーメッセージ
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis30004(String errorMessage) {
        return new SimpleMessageCode("NORTIS30004", errorMessage, new Object[] {});
    }

    /**
     * NORTIS30005
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis30005() {
        return new SimpleMessageCode("NORTIS30005", "指定されたメッセージコンシューマは存在しません", new Object[] {});
    }

    /**
     * NORTIS30006
     * 
     * @param endpointId エンドポイントID
     * @return {@link MessageCode}
     */
    public static MessageCode nortis30006(String endpointId) {
        return new SimpleMessageCode("NORTIS30006", "エンドポイントID: %s", new Object[] { endpointId });
    }

    /**
     * NORTIS40001
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis40001() {
        return new SimpleMessageCode("NORTIS40001", "受信イベントの消費処理に失敗しました", new Object[] {});
    }

    /**
     * NORTIS40002
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis40002() {
        return new SimpleMessageCode("NORTIS40002", "JSONへの変換に失敗しました", new Object[] {});
    }

    /**
     * NORTIS40003
     * 
     * @param consumerId コンシューマID
     * @return {@link MessageCode}
     */
    public static MessageCode nortis40003(String consumerId) {
        return new SimpleMessageCode("NORTIS40003", "コンシューマで対象のテキストタイプのメッセージが存在しません。コンシューマID: %s",
                new Object[] { consumerId });
    }

    /**
     * NORTIS50001
     * 
     * @param tenantId テナントID
     * @param userId   ユーザID
     * @param roleId   ロールID
     * @return {@link MessageCode}
     */
    @Deprecated
    public static MessageCode nortis50001(String tenantId, String userId, String roleId) {
        return new SimpleMessageCode("NORTIS50001", "このユーザはこのロールをすでに存在します。テナントID: %s, ユーザID: %s, ロールID: %s",
                new Object[] { tenantId });
    }

    /**
     * NORTIS50002
     * 
     * @param tenantId テナントID
     * @return {@link MessageCode}
     */
    @Deprecated
    public static MessageCode nortis50002(String tenantId) {
        return new SimpleMessageCode("NORTIS50002", "指定されたテナントへの権限はすでにありません。テナントID: %s, ユーザID: %s, ロールID: %s",
                new Object[] { tenantId });
    }

    /**
     * NORTIS50003
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis50003() {
        return new SimpleMessageCode("NORTIS50003", "指定されたログインIDのユーザはすでに存在します。", new Object[] {});
    }

    /**
     * NORTIS50004
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis50004() {
        return new SimpleMessageCode("NORTIS50004", "指定されたユーザまたはパスワードは存在しません", new Object[] {});
    }

    /**
     * NORTIS50005
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis50005() {
        return new SimpleMessageCode("NORTIS50005", "権限がありません", new Object[] {});
    }

    /**
     * NORTIS50006
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis50006() {
        return new SimpleMessageCode("NORTIS50006", "採番しているユーザIDが重複しています", new Object[] {});
    }

    /**
     * NORTIS60001
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis60001() {
        return new SimpleMessageCode("NORTIS60001", "認証エラーです", new Object[] {});
    }

    /**
     * NORTIS60002
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis60002() {
        return new SimpleMessageCode("NORTIS60002", "APIキーが期限切れです。再認証してください", new Object[] {});
    }

    /**
     * NORTIS60003
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis60003() {
        return new SimpleMessageCode("NORTIS60003", "ログインの有効期限切れの処理に失敗しました", new Object[] {});
    }

    /**
     * NORTIS70001
     * 
     * @param checkLength パスワードのチェック桁数
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis70001(int checkLength) {
        return new SimpleMessageCode("NORTIS70001", "パスワードは%s文字以上である必要があります".formatted(checkLength),
                new Object[] { checkLength });
    }

    /**
     * NORTIS90001
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis90001() {
        return new SimpleMessageCode("NORTIS90001", "メールのメッセージの作成に失敗しました", new Object[] {});
    }

    /**
     * NORTIS90002
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis90002() {
        return new SimpleMessageCode("NORTIS90002", "メールの送信に失敗しました", new Object[] {});
    }

    /**
     * NORTIS00003
     * 
     * @return {@link MessageCode}
     */
    public static MessageCode nortis90003() {
        return new SimpleMessageCode("NORTIS90003", "テンプレートの書き込みに失敗しました", new Object[] {});
    }

}
