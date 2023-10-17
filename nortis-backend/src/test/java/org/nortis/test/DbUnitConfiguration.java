package org.nortis.test;

import java.util.function.Supplier;
import javax.sql.DataSource;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/**
 * DBUnitのコンポーネントを準備するコンフィグです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
@TestConfiguration
public class DbUnitConfiguration {

	@Bean
	IDatabaseTester databseTester(DataSource datasource) {
		return new DataSourceDatabaseTester(datasource);
	}
	
	@Bean
	Supplier<DbUnitTableAssert> dbUnitAssert(IDatabaseTester databaseTester, ResourceLoader resourceLoader) {
		return () -> new DbUnitTableAssert(databaseTester, resourceLoader);
	}
	
}
