package test;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DaoTest {

	// This path is relative to ${project_root}/src/test/resources
	// This path is used in building the absolute path for the database
	private static final String DB_PATH = "/database/MyDbFile.db";
	// This will contain the absolute file path to the database
	private String dbPath;

	@Before
	public void setUp() throws Exception {
		String path = getClass().getResource(DB_PATH).toURI().getPath();
		File dbFile = new File(path);
		// assertThat(dbFile.exists()).isTrue();
		dbPath = dbFile.getAbsolutePath();

		// Perform any other necessary set-up operations...
	}

	@After
	public void tearDown() throws Exception {
		// Perform any necessary clean-up operations...
	}

	@Test
	public void testGet() throws Exception {
		// SQLiteDatabase db = SQLiteDatabase.open(dbPath, null,
		// SQLiteDatabase.OPEN_READWRITE);
		//
		// // Perform database operations...
		//
		// // Perform assertions on query results...
		//
		// db.close();
	}
}
