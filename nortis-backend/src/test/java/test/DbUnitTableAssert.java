package test;

import java.io.IOException;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.ResourceLoader;

/**
 * DBUnitの検証の拡張クラスです
 * @author yoshiokahiroshi
 * @version 1.0.0
 */
public class DbUnitTableAssert {

	private final IDatabaseTester databaseTester;

	private final ResourceLoader resourceLoader;
	
	private ITable expect;
	
	private ITable actual;
	
	/**
	 * インスタンスを生成します
	 * @param databaseTester {@link IDatabaseTester}
	 * @param resourceLoader {@link ResourceLoader}
	 */
	public DbUnitTableAssert(IDatabaseTester databaseTester, ResourceLoader resourceLoader) {
		this.databaseTester = databaseTester;
		this.resourceLoader = resourceLoader;
	}
	
	/**
	 * 期待値を設定します
	 * @param xmlFilePath XMLのパス
	 * @param tableName テーブル名
	 * @return このインスタンス
	 */
	public DbUnitTableAssert expect(String xmlFilePath, String tableName) {
		IDataSet expectedDataSet;
		try {
			expectedDataSet = new FlatXmlDataSetBuilder().build(this.resourceLoader.getResource(xmlFilePath).getFile());
			this.expect = expectedDataSet.getTable("RECEIVE_EVENT");
		} catch (DataSetException | IOException e) {
			throw new IllegalStateException("DBUnitで異常が発生しました", e);
		}
		return this;
	}

	/**
	 * 実際の値を設定します
	 * @param tableName テーブル名
	 * @param sql SQL
	 * @return このインスタンス
	 */
	public DbUnitTableAssert actual(String tableName, String sql) {
		try {
			this.actual  = this.databaseTester.getConnection().createQueryTable(tableName, sql);
		} catch (Exception e) {
			throw new IllegalStateException("DBUnitで異常が発生しました", e);
		}
		return this;
	}

	/**
	 * 検証を実行します
	 * @throws DatabaseUnitException DBUnitのエラー
	 */
	public void assertThat() throws DatabaseUnitException {
		Assertion.assertEquals(expect, actual);
	}
}
