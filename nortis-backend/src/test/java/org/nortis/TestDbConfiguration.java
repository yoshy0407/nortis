package org.nortis;

import org.nortis.infrastructure.config.DomaConfiguration;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.context.annotation.Import;

/**
 * DB関連のテストでの設定を行うコンフィグ
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@AutoConfigureDataJdbc
@Import({ DomaAutoConfiguration.class, DomaConfiguration.class })
public class TestDbConfiguration {

}
