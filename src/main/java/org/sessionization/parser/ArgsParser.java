package org.sessionization.parser;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.NamedOptionDef;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArgsParser {

	private class Param implements Parameters {

		private String param;

		public Param(String param) {
			this.param = param;
		}

		@Override
		public String getParameter(int idx) throws CmdLineException {
			return param;
		}

		@Override
		public int size() {
			return 1;
		}
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

	@Option(name = "-c", aliases = "crawlers", usage = "Ignore web crawlers", metaVar = "<bool>")
	private boolean ignoreCrawlers = false;

	@Option(name = "-h", aliases = {"--help", "-?"}, usage = "Prints this message", hidden = true)
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

	@Option(name = "-xml", usage = "Path to configuration file", metaVar = "<path>")
	private File configFile = null;

	@Option(name = "-dbdic", aliases = "database.dialect.class", usage = "Path to class file, that is dialect for database", metaVar = "<path>")
	private URL dialect = null;

	@Option(name = "-in", aliases = "inputfile", usage = "Input log file", metaVar = "<path>")
	private File inputFile = null;

	@Option(name = "-dbddl", aliases = "database.ddl", usage = "Create new tables or update exsisting ones", metaVar = "<create|update>")
	private DdlOperation operation = DdlOperation.Create;

	private URL driverUrl = null;

	private List<String> logFormat = null;

	private Locale locale = null;

	public ArgsParser(String[] args) throws CmdLineException, URISyntaxException {
		CmdLineParser parser = new CmdLineParser(this);
		parser.parseArgument(args);
		if (configFile != null) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(configFile);
				if (document.getFirstChild().getNodeName().equals("spletneseje-configuration")) {
					setFields(parser.getOptions(), document.getFirstChild());
				} else {
					throw new CmdLineException(parser, new Exception("Bad xml file!!!"));
				}
			} catch (ParserConfigurationException e) {
				throw new CmdLineException(parser, e);
			} catch (SAXException e) {
				throw new CmdLineException(parser, e);
			} catch (IOException e) {
				throw new CmdLineException(parser, e);
			}
		}
		if (printHelp) throw new CmdLineException(parser, new Exception());
		if (inputFile == null) throw new CmdLineException(parser, new Exception("Missing input file!!!"));
		if (databaseUrl == null) databaseUrl = new URI("jdbc:sqlite:sqliteDB");
		// todo preveri odvisne argumente, se so nastavljeni
	}

	private void setFields(List<OptionHandler> options, Node node) throws CmdLineException {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) if ("property".equals(list.item(i).getNodeName())) {
			String value = list.item(i).getAttributes().getNamedItem("name").getNodeValue();
			OptionHandler handler = findOptionByName(value, options);
			handler.parseArguments(new Param(list.item(i).getTextContent()));
		}
	}

	private OptionHandler findOptionByName(String name, List<OptionHandler> list) {
		for (OptionHandler h : list) {
			NamedOptionDef optionDef = (NamedOptionDef) h.option;
			for (String alisa : optionDef.aliases()) if (name.equals(alisa)) {
				return h;
			}
		}
		return null;
	}

	@Option(name = "-flo", aliases = "format.locale", usage = "Locale for time parsing. Check ISO 639 standard for names.", metaVar = "<locale>")
	public void setLocale(String locale) {
		this.locale = new Locale(locale);
	}

	@Option(name = "-fl", aliases = "format.log", usage = "Log file format. Check NCSA or W3C log formats.", metaVar = "<log format>")
	public void setLogFormat(String format) {
		logFormat = new ArrayList<>();
		for (String symbol : format.split(" ")) logFormat.add(symbol);
	}

	@Option(name = "-dbdr", aliases = "database.driver", usage = "Path to jar file, that is a driver", metaVar = "<path>")
	public void setDriverUrl(File file) throws MalformedURLException {
		driverUrl = file.toURI().toURL();
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

	public File getInputFile() {
		return inputFile;
	}

	public URL getDriverUrl() {
		return driverUrl;
	}

	public File getConfigFile() {
		return configFile;
	}

	public Locale getLocale() {
		return locale;
	}

	public boolean ignoreCrawlers() {
		return ignoreCrawlers;
	}

	public List<String> getLogFormat() {
		return logFormat;
	}

	public File getInputFilePath() {
		return inputFile;
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
}
