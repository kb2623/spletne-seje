package org.oosql;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("unused")
public class DataBase implements AutoCloseable {

	private SqlDriver driver;

	public DataBase(String protocol, File file, Properties properties) throws FileNotFoundException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		if (file.isFile()) driver = new SqlDriver(protocol, file, properties);
		else throw new FileNotFoundException("[" + file.getPath() + "] is not a file");
	}

	public DataBase(String protocol, String path, Properties properties) throws FileNotFoundException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		this(protocol, new File(path), properties);
	}


	public void createTables(Object in) {
		// TODO
	}

	public void insert(Object input) {
		// TODO
	}

	public Object lookup(Object input, Class... conditions) {
		// TODO
		return null;
	}

	@Deprecated
	protected SqlDriver getDataBase() {
		return driver;
	}

	@Override
	public void close() throws SQLException {
		driver.close();
	}
}
