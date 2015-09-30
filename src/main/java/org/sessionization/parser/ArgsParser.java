package org.sessionization.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.sessionization.fields.Field;

@SuppressWarnings("deprecation")
public class ArgsParser {

	@Option(name = "-c", usage = "Ignore web crawlers")
	private boolean ignoreCrawlers = false;

	@Option(name = "-h", usage = "Prints this message")
	private boolean printHelp = false;

	private List<String> logFormat = null;

	@Option(name = "-df", usage = "Date format", metaVar = "<date format>")
	private String dateFormat = null;

	@Option(name = "-tf", usage = "Time format", metaVar = "<time format>")
	private String timeFormat = null;

	private File configFile = null;

	private URL driverUrl = null;

	private File inputFile;

	private Locale locale = null;

	public ArgsParser(String[] args) throws CmdLineException {
		CmdLineParser parser = new CmdLineParser(this);
		parser.parseArgument(args);
		if(printHelp) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			parser.printUsage(stream);
			throw new CmdLineException(parser, "usage: SpletneSeje [Options]\n" + stream.toString()+"\n" + "Example:\n" + "SpletneSeje "+parser.printExample(ExampleMode.ALL));
		}
		if(inputFile == null) throw new CmdLineException(parser, "Input file is required");
	}

	@Option(name = "-in", usage = "Input log file", metaVar = "<path>")
	public void setInputFile(String path) {
		File file = new File(path);
		if (!file.isFile()) throw new RuntimeException("Bad input file");
		inputFile = file;
	}

	@Option(name = "-lc", usage = "Locale for time parsing. Check ISO 639 standard for names.", metaVar = "<locale>")
	public void setLocale(String locale) {
		this.locale = new Locale(locale);
	}

	@Option(name = "-ff", usage = "Log file format", metaVar = "<log format>")
	public void getLogFormat(String format) {
		logFormat = new ArrayList<>();
		for (String symbol : format.split(" ")) logFormat.add(symbol);
	}

	@Option(name = "-cfg", usage = "Path to configuration file", metaVar = "<path>")
	public void setConfigFile(String path) throws RuntimeException {
		File file = new File(path);
		if (!file.isFile()) throw new RuntimeException("Bad configuration file path");
		configFile = file;
	}

	@Option(name = "-d", usage = "Path to jar file, that is a driver", metaVar = "<path>")
	public void setDriverUrl(String path) throws MalformedURLException {
		File file = new File(path);
		driverUrl = file.toURI().toURL();
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

}
