package spletneseje.parser;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Locale;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

@SuppressWarnings("deprecation")
public class ArgsParser {

	@Option(name="-c", usage="Ignore web crawlers")
	private boolean ignoreCrawlers = false;

	@Option(name="-h", usage="Prints this message")
	private boolean printHelp = false;

	@Option(name="-ff", usage="Log file format", metaVar="<log format>")
	private String logFormat;

	@Option(name="-df", usage="Date format", metaVar="<date format>")
	private String dateFormat;

	@Option(name="-tf", usage="Time format", metaVar="<time format>")
	private String timeFormat;

	@Option(name="-in", usage="Input log file", metaVar="<path>")
	private String inputFile;

	@Option(name="-out", usage="Output file", metaVar="<path>")
	private String outputFile = "SpletneSejeDB"+LocalDateTime.now().toString();

	private Locale locale;

	@Option(name="-d", usage="Delete output file before witing")
	private boolean deleteFile = false;

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

	@Option(name="-l", usage="Locale for time parsing", metaVar="<locale>")
	public void setLocale(String locale) {
		this.locale = new Locale(locale);
	}

	public Locale getLocale() {
		return locale;
	}

	public boolean ignoreCrawlers() {
		return ignoreCrawlers;
	}

	public String getLogFormat() {
		return (logFormat != null) ? logFormat.toUpperCase() : null;
	}

	public String getInputFilePath() {
		return inputFile;
	}

	public String getOutputFilePath() {
		return outputFile;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public boolean deleteOutputFile() {
		return deleteFile;
	}

}
