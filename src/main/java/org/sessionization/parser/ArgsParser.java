package org.sessionization.parser;

import org.kohsuke.args4j.*;

import java.io.File;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class ArgsParser {

	private Properties properties;
	private CmdLineParser parser;

	@Option(name = "-h", aliases = {"--help", "-?"}, usage = "Prints this message", hidden = true, help = true)
	private boolean printHelp = false;

	@Argument(usage = "Input log files", metaVar = "<path>", required = true, multiValued = true)
	private File[] inputFile = null;

	public ArgsParser(String... args) throws CmdLineException, URISyntaxException {
		initDefaults();
		parser = new PropretiesCmdParser(this);
		parser.parseArgument(args);
		if (printHelp) {
			throw new CmdLineException(parser, "Print help");
		}
	}

	private void initDefaults() {
		properties = new Properties();
		properties.setProperty("crawlers", String.valueOf(false));
		properties.setProperty("database.driver.class", "org.sqlite.JDBC");
		properties.setProperty("database.dialect.class", "org.dialect.SQLiteDialect");
		properties.setProperty("database.ddl", String.valueOf(DdlOperation.Create));
		properties.setProperty("database.connection.pool_size", "1");
		properties.setProperty("database.url", "jdbc:sqlite:sqliteDB");
		properties.setProperty("database.sql.show", String.valueOf(false));
		properties.setProperty("database.sql.show.format", String.valueOf(false));
		properties.setProperty("format.locale", Locale.US.toLanguageTag());
		properties.setProperty("parse.size", "500");
		properties.setProperty("session.time", "7200");
	}

	public String[] getLogFormat() {
		List<String> tmp = new ArrayList<>();
		for (String symbol : properties.getProperty("format.log").split(" ")) {
			tmp.add(symbol);
		}
		return tmp.toArray(new String[tmp.size()]);
	}

	@Option(name = "-fl", aliases = "format.log", usage = "Log file format. Check NCSA or W3C log formats.", metaVar = "<log format>")
	public void setLogFormat(String format) {
		properties.setProperty("format.log", format);
	}

	public int getSessionTime() {
		return Integer.valueOf(properties.getProperty("session.time"));
	}

	@Option(name = "-st", aliases = "session.time", usage = "Time until session is over. Time is represented in seconds.", metaVar = "<int>")
	public void setSessionTime(int timeInSecons) {
		properties.setProperty("session.time", String.valueOf(timeInSecons));
	}

	public int getParseSize() {
		return Integer.valueOf(properties.getProperty("parse.size"));
	}

	@Option(name = "-ps", aliases = "parse.size", usage = "Number of sessions", metaVar = "<int>")
	public void setParseSize(int size) {
		properties.setProperty("parse.size", String.valueOf(size));
	}

	public String getTimeFormat() {
		return properties.getProperty("format.time");
	}

	@Option(name = "-ft", aliases = "format.time", usage = "Time format", metaVar = "<time format>")
	public void setTimeFormat(String line) {
		properties.setProperty("format.time", line);
	}

	public URI getDatabaseUrl() {
		try {
			return new URI(properties.getProperty("database.url"));
		} catch (URISyntaxException e) {
			return null;
		}
	}

	@Option(name = "-dburl", aliases = "database.url", usage = "URL to database", metaVar = "<URL>")
	public void setDatabaseUrl(URI uri) {
		properties.setProperty("database.url", uri.toASCIIString());
	}

	public Locale getLocale() {
		return Locale.forLanguageTag(properties.getProperty("format.locale"));
	}

	@Option(name = "-flo", aliases = "format.locale", usage = "Locale for time parsing. Check lahguage tags.", metaVar = "<tag>")
	public void setLocale(String locale) {
		properties.setProperty("format.locale", locale);
	}

	public String getUserName() {
		return properties.getProperty("database.username");
	}

	@Option(name = "-dbun", aliases = "database.username", usage = "User name for database", metaVar = "<string>")
	public void setUserName(String name) {
		properties.setProperty("database.username", name);
	}

	public String getPassWord() {
		return properties.getProperty("database.password");
	}

	@Option(name = "-dbpw", aliases = "database.password", usage = "Password for user", metaVar = "<string>")
	public void setPassWord(String pass) {
		properties.setProperty("database.password", pass);
	}

	public String getDateFormat() {
		return properties.getProperty("format.date");
	}

	@Option(name = "-fd", aliases = "format.date", usage = "Date format", metaVar = "<date format>")
	public void setDateFormat(String line) {
		properties.setProperty("format.date", line);
	}

	public String getDialectClass() {
		return properties.getProperty("database.dialect.class");
	}

	@Option(name = "-dbdi", aliases = "database.dialect", usage = "Database dialect class name", metaVar = "<string>")
	public void setDialectClass(String name) {
		properties.setProperty("database.dialect", name);
	}

	public DdlOperation getOperation() {
		return Enum.valueOf(DdlOperation.class, properties.getProperty("database.ddl"));
	}

	@Option(name = "-dbddl", aliases = "database.ddl", usage = "Create new tables or update exsisting ones", metaVar = "<create|update>")
	public void setOperation(DdlOperation opt) {
		properties.setProperty("database.ddl", String.valueOf(opt));
	}

	public String getDriverClass() {
		return properties.getProperty("database.driver.class");
	}

	@Option(name = "-dbdrc", aliases = "database.driver.class", usage = "Driver class for connecting to databse", metaVar = "<string>")
	public void setDriverClass(String name) {
		properties.setProperty("database.driver.class", name);
	}

	public URL getDriverUrl() {
		try {
			return new URL(properties.getProperty("database.driver"));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Option(name = "-dbdr", aliases = "database.driver", usage = "Path to jar file, that is a driver", metaVar = "<path>", depends = "-dbdrc")
	public void setDriverUrl(File file) throws MalformedURLException {
		properties.setProperty("database.driver", file.getPath());
	}

	public int getConnectoinPoolSize() {
		return Integer.valueOf(properties.getProperty("database.connection.pool_size"));
	}

	@Option(name = "-dbcp", aliases = "database.connection.pool_size", usage = "Number of connetions alowed for connecting to database", metaVar = "<int>")
	public void setConnectoinPoolSize(int size) {
		properties.setProperty("database.connection.pool_size", String.valueOf(size));
	}

	public boolean isShowSql() {
		return Boolean.valueOf(properties.getProperty("database.sql.show"));
	}

	@Option(name = "-dbsq", aliases = "database.sql.show", usage = "Show sql querys", metaVar = "<bool>")
	public void setShowSql(boolean opt) {
		properties.setProperty("database.sql.show", String.valueOf(opt));
	}

	public URL getDialect() {
		try {
			return new URL(properties.getProperty("database.dialect"));
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Option(name = "-dbdic", aliases = "database.dialect.class", usage = "Path to class file, that is dialect for database", metaVar = "<path>", depends = "-dbdi")
	public void setDialect(URL url) {
		properties.setProperty("", "");
	}

	public boolean isIgnoreCrawlers() {
		return Boolean.valueOf(properties.getProperty("crawlers"));
	}

	@Option(name = "-c", aliases = "crawlers", usage = "Ignore web crawlers", metaVar = "<bool>")
	public void setIgnoreCrawlers(boolean opt) {
		properties.setProperty("crawlers", String.valueOf(opt));
	}

	public boolean isShowSqlFormat() {
		return Boolean.valueOf(properties.getProperty("database.sql.show.format"));
	}

	@Option(name = "-dbsqf", aliases = "database.sql.show.format", usage = "Show formated sql querys", metaVar = "<bool>", depends = "-dbsq")
	public void setShowSqlFormat(boolean opt) {
		properties.setProperty("database.sql.show.format", String.valueOf(opt));
	}

	public String[] getIgnoreFields() {
		List<String> tmp = new ArrayList<>();
		String ignore = properties.getProperty("log.ignore");
		if (ignore != null) {
			for (String s : properties.getProperty("log.ignore").split(" ")) {
				tmp.add(s);
			}
			return tmp.toArray(new String[tmp.size()]);
		} else {
			return new String[0];
		}
	}

	@Option(name = "li", aliases = "log.ignore", usage = "Fields to ignore", metaVar = "<string>")
	public void setIgnoreFields(String niz) {
		properties.setProperty("log.ignore", niz);
	}

	public void printHelp(OutputStream out) {
		parser.printUsage(out);
	}

	public boolean isPrintHelp() {
		return printHelp;
	}

	public File[] getInputFile() {
		return inputFile;
	}

	public enum DdlOperation {
		Create {
			@Override
			public String getValue() {
				return "create";
			}
		},
		Update {
			@Override
			public String getValue() {
				return "update";
			}
		};

		public abstract String getValue();
	}

}
