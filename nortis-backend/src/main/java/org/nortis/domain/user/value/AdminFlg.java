package org.nortis.domain.user.value;

import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 管理者権限を表すオブジェクトです
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@Getter
@Domain(valueType = String.class, accessorMethod = "getValue", factoryMethod = "resolve")
public enum AdminFlg {
    /**
     * 管理者ユーザ
     */
    ADMIN("1", "管理者ユーザ"),
    /**
     * 通常ユーザ
     */
    MEMBER("0", "通常ユーザ");

    private final String value;

    private final String displayName;

    private AdminFlg(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     * {@code boolean}で値を取得します
     * 
     * @return 値
     */
    public boolean booleanValue() {
        if (this.value.equals("1")) {
            return true;
        }
        if (this.value.equals("0")) {
            return false;
        }
        return false;
    }

    /**
     * 値に対応する管理者フラグを取得します
     * 
     * @param value 値
     * @return ログインフラグ
     */
    public static AdminFlg resolve(String value) {
        for (AdminFlg adminFlg : AdminFlg.values()) {
            if (adminFlg.getValue().equals(value)) {
                return adminFlg;
            }
        }
        throw new IllegalStateException("指定された値が存在しません。　値：" + value);
    }

}
