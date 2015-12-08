package org.sessionization;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.kohsuke.args4j.CmdLineException;
import org.sessionization.analyzer.LogAnalyzer;
import org.sessionization.parser.*;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserIdAbs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpletneSeje {

	private ArgsParser argsParser;
	private AbsParser logParser;
	private HibernateUtil db;

	public SpletneSeje() {
		argsParser = null;
		logParser = null;
		db = null;
	}
	/**
	 *
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
				logParser = new NCSAParser(argsParser.getLocale(), argsParser.getInputFile(), analyzer.getFields());
				break;
			case W3C:
				logParser = new W3CParser(argsParser.getLocale(), argsParser.getInputFile());
				break;
			case IIS:
				logParser = new IISParser(argsParser.getLocale(), argsParser.getInputFile(), analyzer.getFields());
				break;
			default:
				throw new ParseException("Unknow format of input log file!!!", 0);
			}
			logParser.openFile(argsParser.getInputFile());
			break;
		case "COMMON":
			logParser = new NCSAParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.CommonLogFormat.create(LogAnalyzer.hasCombinedCookie()));
			break;
		case "COMBINED":
			logParser = new NCSAParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.CombinedLogFormat.create(LogAnalyzer.hasCombinedCookie()));
			break;
		case "CUSTOM":
			if (argsParser.getLogFormat().length > 1) {
				logParser = new NCSAParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.CustomLogFormat.create(argsParser.getLogFormat()));
			} else {
				throw new ExceptionInInitializerError("Need more args!!!");
			}
			break;
		case "EXTENDED":
			logParser = new W3CParser(argsParser.getLocale(), argsParser.getInputFile());
			break;
		case "IIS":
			logParser = new IISParser(argsParser.getLocale(), argsParser.getInputFile(), LogFormats.IISLogFormat.create(argsParser.getLogFormat()));
			break;
		default:
			throw new ExceptionInInitializerError("Unknown log format!!!");
		}

		/** Nastavi format datuma */
		if(argsParser.getDateFormat() != null) {
			if(logParser instanceof NCSAParser) {
				((NCSAParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else if(logParser instanceof W3CParser) {
				((W3CParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else {
				((IISParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			}
		}

		/** Nastavi format ure */
		if(argsParser.getTimeFormat() != null) {
			if(logParser instanceof NCSAParser) {
				System.err.println("ignoring -tf \"" + argsParser.getTimeFormat() + "\"");
			} else if(logParser instanceof W3CParser) {
				((W3CParser) logParser).setTimeFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else {
				((IISParser) logParser).setTimeFormat(argsParser.getDateFormat(), argsParser.getLocale());
			}
		}

		if (logParser instanceof W3CParser) {
			for (ParsedLine l : logParser) {
				if (logParser.getFieldType() != null) break;
			}
		}

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
			System.err.println(e.getLocalizedMessage());
			System.exit(2);
		} catch (FileNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(3);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(4);
		} catch (URISyntaxException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(5);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(6);
		} catch (ExceptionInInitializerError e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(7);
		} catch (InterruptedException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(8);
		} catch (CannotCompileException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(9);
		} catch (NotFoundException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(10);
		}
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
	}

}
