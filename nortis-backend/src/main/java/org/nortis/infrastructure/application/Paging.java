package org.nortis.infrastructure.application;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * ページングに関するクラスです
 * 
 * @author yoshiokahiroshi
 * @version
 */
@ToString
@AllArgsConstructor
public class Paging {

    private final int pageNo;

    private final int pagePersize;

    /**
     * ページ番号に対するオフセットを取得します
     * 
     * @return オフセット
     */
    public int offset() {
        return this.pagePersize * (pageNo - 1);
    }

    /**
     * ページ番号に対するリミットを取得します
     * 
     * @return リミット
     */
    public int limit() {
        return this.pagePersize;
    }

}
