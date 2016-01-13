package org.sessionization;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.datastruct.ObjectPool;
import org.kohsuke.args4j.CmdLineException;
import org.sessionization.analyzer.LogAnalyzer;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.*;
import org.sessionization.parser.datastruct.UserIdAbs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpletneSeje {

	private ArgsParser argsParser;
	private WebLogParser logParser;
	private HibernateUtil db;

	public SpletneSeje() {
		argsParser = null;
		logParser = null;
		db = null;
	}

	/**
	 * @param args
	 * @throws CmdLineException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("deprecation")
	public SpletneSeje(String[] args) throws CmdLineException, URISyntaxException, IOException, ParseException, ClassNotFoundException, CannotCompileException, NotFoundException {
		/** Parsanje vhodnih argumentov */
		argsParser = new ArgsParser(args);
		/** Preveri format in nastavi tipe polji v datoteki */
		switch ((argsParser.getLogFormat() != null) ? argsParser.getLogFormat()[0] : "") {
			case "":
				LogAnalyzer analyzer = new LogAnalyzer(argsParser.getInputFile());
				switch (analyzer.getLogFileType()) {
					case NCSA:
						logParser = new NCSAWebLogParser(argsParser.getLocale(), argsParser.getInputFile(), analyzer.getFields());
						break;
					case W3C:
						logParser = new W3CWebLogParser(argsParser.getLocale(), argsParser.getInputFile());
						break;
					case IIS:
						logParser = new IISWebLogParser(argsParser.getLocale(), argsParser.getInputFile(), analyzer.getFields());
						break;
					default:
						throw new ParseException("Unknow format of input log file!!!", 0);
				}
				logParser.openFile(argsParser.getInputFile());
				break;
			case "COMMON":
				logParser = new NCSAWebLogParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.CommonLogFormat.make());
				break;
			case "COMBINED":
				logParser = new NCSAWebLogParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.CombinedLogFormat.make());
				break;
			case "CUSTOM":
				if (argsParser.getLogFormat().length > 1) {
					logParser = new NCSAWebLogParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.ParseCmdArgs.make(argsParser.getLogFormat()));
				} else {
					throw new ExceptionInInitializerError("Need more args!!!");
				}
				break;
			case "EXTENDED":
				logParser = new W3CWebLogParser(argsParser.getLocale(), argsParser.getInputFile());
				break;
			case "IIS":
				logParser = new IISWebLogParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.ParseCmdArgs.make(argsParser.getLogFormat()));
				break;
			default:
				throw new ExceptionInInitializerError("Unknown log format!!!");
		}
		/** Nastavi format datuma */
		if (argsParser.getDateFormat() != null) {
			if (logParser instanceof NCSAWebLogParser) {
				((NCSAWebLogParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else if (logParser instanceof W3CWebLogParser) {
				((W3CWebLogParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else {
				((IISWebLogParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			}
		}
		/** Nastavi format ure */
		if (argsParser.getTimeFormat() != null) {
			if (logParser instanceof NCSAWebLogParser) {
				System.err.println("ignoring -tf \"" + argsParser.getTimeFormat() + "\"");
			} else if (logParser instanceof W3CWebLogParser) {
				((W3CWebLogParser) logParser).setTimeFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else {
				((IISWebLogParser) logParser).setTimeFormat(argsParser.getDateFormat(), argsParser.getLocale());
			}
		}
		/** Dodaj polja, ki jih ignoriramo */
		String[] ignore = argsParser.getIgnoreFields();
		if (ignore.length > 0) {
			logParser.setIgnoreFieldType(LogFormats.ParseCmdArgs.make(ignore));
		}
		/** Nastavi Object pool in dodaj Creatorje in Propertije */
		ObjectPool pool = new ObjectPool();
		Map creators = new HashMap<>();
		List<LogFieldType> fields = logParser.getFieldType();
		if (logParser.getIgnoreFieldTypes() != null) {
			fields.removeAll(logParser.getIgnoreFieldTypes());
		}
		for (LogFieldType f : fields) {
			Map tmp = f.getCreators();
			if (tmp != null) {
				creators.putAll(tmp);
			}
		}
		pool.setCreators(creators);
		pool.setProperties(argsParser.getObjectPoolProperties());
		logParser.setPool(pool);
		/** Ustvari povezavo do podatkovne baze, ter ustvari tabele */
		db = new HibernateUtil(argsParser, logParser);
	}

	/**
	 * @param args
	 */
	public static void main(String... args) {
		try {
			new SpletneSeje(args).run();
		} catch (CmdLineException e) {
			if (!e.getLocalizedMessage().equals("Print help")) {
				System.err.println(e.getLocalizedMessage());
			} else {
				e.getParser().printUsage(System.out);
				System.out.print("\nUsage:\nProgName");
				e.getParser().printSingleLineUsage(System.out);
			}
			System.exit(1);
		} catch (ParseException e) {
			printError(e.getLocalizedMessage(), 2);
		} catch (FileNotFoundException e) {
			printError(e.getLocalizedMessage(), 3);
		} catch (IOException e) {
			printError(e.getLocalizedMessage(), 4);
		} catch (URISyntaxException e) {
			printError(e.getLocalizedMessage(), 5);
		} catch (ClassNotFoundException e) {
			printError(e.getLocalizedMessage(), 6);
		} catch (ExceptionInInitializerError e) {
			printError(e.getCause().getMessage(), 7);
		} catch (InterruptedException e) {
			printError(e.getLocalizedMessage(), 8);
		} catch (CannotCompileException e) {
			printError(e.getLocalizedMessage(), 9);
		} catch (NotFoundException e) {
			printError(e.getLocalizedMessage(), 10);
		}
	}

	private static void printError(String message, int exit) {
		System.err.println("Error: " + message);
		System.exit(exit);
	}

	public void run() throws InterruptedException {
		BlockingQueue<Map<String, UserIdAbs>> qParserLearner = new LinkedBlockingQueue<>();
		Thread parseThread = new ParserThread(qParserLearner, logParser, argsParser.getParseSize());
		Thread learnThread = new LearnThread(qParserLearner);
		parseThread.start();
		learnThread.start();
		// TODO dodaj se druge niti
		try {
			parseThread.join();
		} catch (InterruptedException e) {
			throw new InterruptedException(e.getLocalizedMessage() + " :: problem in parsing!!!");
		}
		try {
			learnThread.join();
		} catch (InterruptedException e) {
			throw new InterruptedException(e.getLocalizedMessage() + " :: problem in learning!!!");
		}
		logParser.close();
	}
}
