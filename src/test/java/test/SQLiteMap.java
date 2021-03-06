package test;

import java.io.File;
import java.sql.ResultSet;

import org.robolectric.util.DatabaseConfig;

public class SQLiteMap implements DatabaseConfig.DatabaseMap {

	private String _dbFile;

	/**
	 * This constructor will use in-memory database.
	 */
	public SQLiteMap() {
	}

	/**
	 * This constructor will use a database file
	 * 
	 * @param dbFile
	 *            : path to the SQLite database file
	 */
	public SQLiteMap(String dbFile) {
		_dbFile = dbFile;
	}

	public String getDriverClassName() {
		return "org.sqlite.JDBC";
	}

	public String getConnectionString(final File f) {
		if (_dbFile == null)
			return "jdbc:sqlite::memory:";
		else
			return String.format("jdbc:sqlite:%s", _dbFile);
	}

	public String getScrubSQL(String sql) {
		return sql;
	}

	public String getSelectLastInsertIdentity() {
		return "SELECT last_insert_rowid() AS id";
	}

	public int getResultSetType() {
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	@Override
	public String getMemoryConnectionString() {
		return "jdbc:sqlite::memory:";
	}
}
