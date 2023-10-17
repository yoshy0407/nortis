package org.nortis;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * テストのベースクラス
 * 
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@SpringBootTest(classes = { TestDbConfiguration.class })
public class RepositoryTestBase extends TestBase {

}
