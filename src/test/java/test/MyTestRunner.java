package test;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.DatabaseConfig.DatabaseMap;

public class MyTestRunner extends RobolectricTestRunner {
	public MyTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected DatabaseMap setupDatabaseMap(Class<?> arg0, DatabaseMap arg1) {
		return super.setupDatabaseMap(arg0, arg1);
	}
}
