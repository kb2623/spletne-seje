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

public class ArgsParser {

	private CmdLineParser parser;
	@Option(name = "-c", aliases = "crawlers", usage = "Ignore web crawlers", metaVar = "<bool>")
	private boolean ignoreCrawlers = false;
	@Option(name = "-h", aliases = {"--help", "-?"}, usage = "Prints this message", hidden = true, help = true)
	private boolean printHelp = false;
	@Option(name = "-fd", aliases = "format.date", usage = "Date format", metaVar = "<date format>")
	private String dateFormat = null;
	@Option(name = "-ft", aliases = "format.time", usage = "Time format", metaVar = "<time format>")
	private String timeFormat = null;
	@Option(name = "-dbun", aliases = "database.username", usage = "User name for database", metaVar = "<string>")
	private String userName = null;
	@Option(name = "-dbpw", aliases = "database.password", usage = "Password for user", metaVar = "<string>")
	private String passWord = null;
	@Option(name = "-dbdrc", aliases = "database.driver.class", usage = "Driver class for connecting to databse", metaVar = "<string>")
	private String driverClass = "org.sqlite.JDBC";
	@Option(name = "-dbdi", aliases = "database.dialect", usage = "Database dialect class name", metaVar = "<string>")
	private String dialectClass = "org.dialect.SQLiteDialect";
	@Option(name = "-dburl", aliases = "database.url", usage = "URL to database", metaVar = "<URL>")
	private URI databaseUrl = null;
	@Option(name = "-dbdic", aliases = "database.dialect.class", usage = "Path to class file, that is dialect for database", metaVar = "<path>", depends = "-dbdi")
	private URL dialect = null;
	@Argument(usage = "Input log files", metaVar = "<path>", required = true, multiValued = true)
	private File[] inputFile = null;
	@Option(name = "-dbddl", aliases = "database.ddl", usage = "Create new tables or update exsisting ones", metaVar = "<create|update>")
	private DdlOperation operation = DdlOperation.Create;
	@Option(name = "-dbcp", aliases = "database.connection.pool_size", usage = "Number of connetions alowed for connecting to database", metaVar = "<int>")
	private int connectoinPoolSize = 1;
	@Option(name = "-dbsq", aliases = "database.sql.show", usage = "Show sql querys", metaVar = "<bool>")
	private boolean showSql = false;
	@Option(name = "-dbsqf", aliases = "database.sql.show.format", usage = "Show formated sql querys", metaVar = "<bool>", depends = "-dbsq")
	private boolean showSqlFormat = false;
	@Option(name = "-cc", aliases = "cache", usage = "Do you want to use second-leve cache", metaVar = "<bool>")
	private boolean useCache;
	private URL driverUrl = null;
	private String[] logFormat = null;
	private Locale locale = Locale.US;

	public ArgsParser(String... args) throws CmdLineException, URISyntaxException {
		parser = new XmlCmdParser(this);
		parser.parseArgument(args);
		if (printHelp) {
			throw new CmdLineException(parser, "Print help");
		}
		if (databaseUrl == null) databaseUrl = new URI("jdbc:sqlite:sqliteDB");
	}

	public void printHelp(OutputStream out) {
		parser.printUsage(out);
	}

	public boolean isIgnoreCrawlers() {
		return ignoreCrawlers;
	}

	public boolean isPrintHelp() {
		return printHelp;
	}

	public URL getDialect() {
		return dialect;
	}

	public File[] getInputFile() {
		return inputFile;
	}

	public URL getDriverUrl() {
		return driverUrl;
	}

	@Option(name = "-dbdr", aliases = "database.driver", usage = "Path to jar file, that is a driver", metaVar = "<path>", depends = "-dbdrc")
	public void setDriverUrl(File file) throws MalformedURLException {
		driverUrl = file.toURI().toURL();
	}

	public Locale getLocale() {
		return locale;
	}

	@Option(name = "-flo", aliases = "format.locale", usage = "Locale for time parsing. Check ISO 639 standard for names.", metaVar = "<locale>")
	public void setLocale(String locale) {
		this.locale = new Locale(locale);
	}

	public boolean ignoreCrawlers() {
		return ignoreCrawlers;
	}

	public String[] getLogFormat() {
		return logFormat;
	}

	@Option(name = "-fl", aliases = "format.log", usage = "Log file format. Check NCSA or W3C log formats.", metaVar = "<log format>")
	public void setLogFormat(String format) {
		List<String> tmp = new ArrayList<>();
		for (String symbol : format.split(" ")) {
			tmp.add(symbol);
		}
		logFormat = tmp.toArray(new String[tmp.size()]);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public URI getDatabaseUrl() {
		return databaseUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public String getDialectClass() {
		return dialectClass;
	}

	public DdlOperation getOperation() {
		return operation;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public int getConnectoinPoolSize() {
		return connectoinPoolSize;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public boolean isShowSqlFormat() {
		return showSqlFormat;
	}

	public boolean isUseCache() {
		return useCache;
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
