package org.nortis.domain.event;

import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 受信イベントのリポジトリ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@ConfigAutowireable
@Dao
public interface ReceiveEventRepository {

	/**
	 * 未受信のイベントを取得します
	 * @return 未受信イベントのリスト
	 */
	@Select
	List<ReceiveEvent> notSubscribed();
	
	/**
	 * 保存します
	 * @param receiveEvent 受信イベント
	 * @return 保存件数
	 */
	@Insert
	int save(ReceiveEvent receiveEvent);
	
	/**
	 * 更新します
	 * @param receiveEvent 受信イベント
	 * @return 更新件数
	 */
	@Update
	int update(ReceiveEvent receiveEvent);
	
	/**
	 * 削除します
	 * @param receiveEvent 受信イベント
	 * @return 削除件数
	 */
	@Delete
	int remove(ReceiveEvent receiveEvent);

}
