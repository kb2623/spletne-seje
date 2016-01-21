package org.sessionization;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.datastruct.RadixTreeMap;
import org.datastruct.concurrent.ObjectPool;
import org.datastruct.concurrent.SharedMap;
import org.kohsuke.args4j.CmdLineException;
import org.sessionization.analyzer.LogAnalyzer;
import org.sessionization.database.HibernateUtil;
import org.sessionization.parser.*;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.UserSessionAbs;
import org.sessionization.parser.fields.w3c.MetaData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedTransferQueue;

public class SpletneSeje implements AutoCloseable {

	private ArgumentParser argumentParser;
	private WebLogParser logParser;
	private HibernateUtil db;

	public SpletneSeje() {
		argumentParser = null;
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
		argumentParser = new ArgumentParser(args);
		/** Preveri format in nastavi tipe polji v datoteki */
		switch ((argumentParser.getLogFormat() != null) ? argumentParser.getLogFormat()[0] : "") {
			case "":
				LogAnalyzer analyzer = new LogAnalyzer(argumentParser.getInputFile());
				switch (analyzer.getLogFileType()) {
					case NCSA:
						logParser = new NCSAWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile(), analyzer.getFields());
						break;
					case W3C:
						logParser = new W3CWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile());
						break;
					case IIS:
						logParser = new IISWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile(), analyzer.getFields());
						break;
					default:
						throw new ParseException("Unknow format of input log file!!!", 0);
				}
				logParser.openFile(argumentParser.getInputFile());
				break;
			case "COMMON":
				logParser = new NCSAWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile(), LogFormats.CommonLogFormat.make());
				break;
			case "COMBINED":
				logParser = new NCSAWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile(), LogFormats.CombinedLogFormat.make());
				break;
			case "CUSTOM":
				if (argumentParser.getLogFormat().length > 1) {
					logParser = new NCSAWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile(), LogFormats.ParseCmdArgs.make(argumentParser.getLogFormat()));
				} else {
					throw new ExceptionInInitializerError("Need more args!!!");
				}
				break;
			case "EXTENDED":
				logParser = new W3CWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile());
				break;
			case "IIS":
				logParser = new IISWebLogParser(argumentParser.getLocale(), argumentParser.getInputFile(), LogFormats.ParseCmdArgs.make(argumentParser.getLogFormat()));
				break;
			default:
				throw new ExceptionInInitializerError("Unknown log format!!!");
		}
		/** Ce imamo EXTENDED log format potm moramo pridobiti vrstico, ki ima podatke o poljih v log datoteki */
		if (logParser instanceof W3CWebLogParser) {
			ParsedLine line;
			do {
				line = logParser.parseLine();
				if (line.get(0) instanceof MetaData) {
					MetaData mData = (MetaData) line.get(0);
					if (mData.getMetaData().equals("Fields")) {
						break;
					}
				}
			} while (true);
		}
		/** Nastavi format datuma */
		if (argumentParser.getDateFormat() != null) {
			if (logParser instanceof NCSAWebLogParser) {
				((NCSAWebLogParser) logParser).setDateFormat(argumentParser.getDateFormat(), argumentParser.getLocale());
			} else if (logParser instanceof W3CWebLogParser) {
				((W3CWebLogParser) logParser).setDateFormat(argumentParser.getDateFormat(), argumentParser.getLocale());
			} else {
				((IISWebLogParser) logParser).setDateFormat(argumentParser.getDateFormat(), argumentParser.getLocale());
			}
		}
		/** Nastavi format ure */
		if (argumentParser.getTimeFormat() != null) {
			if (logParser instanceof NCSAWebLogParser) {
				System.err.println("ignoring -tf \"" + argumentParser.getTimeFormat() + "\"");
			} else if (logParser instanceof W3CWebLogParser) {
				((W3CWebLogParser) logParser).setTimeFormat(argumentParser.getDateFormat(), argumentParser.getLocale());
			} else {
				((IISWebLogParser) logParser).setTimeFormat(argumentParser.getDateFormat(), argumentParser.getLocale());
			}
		}
		/** Dodaj polja, ki jih ignoriramo */
		String[] ignore = argumentParser.getIgnoreFields();
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
		pool.setProperties(argumentParser.getObjectPoolProperties());
		logParser.setPool(pool);
		/** Ustvari povezavo do podatkovne baze, ter ustvari tabele */
		db = new HibernateUtil(argumentParser, logParser);
	}

	/**
	 * @param args
	 */
	public static void main(String... args) {
		try (SpletneSeje seje = new SpletneSeje()) {
			seje.run();
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
		} catch (Exception e) {
			printError(e.getLocalizedMessage(), 11);
		}
	}

	private static void printError(String message, int exit) {
		System.err.println("Error: " + message);
		System.exit(exit);
	}

	public void run() throws InterruptedException {
		/** Ustvari vrste za posiljanje podatkov med nitmi */
		BlockingQueue<ParsedLine> parsedLines = new LinkedTransferQueue<>();
		BlockingQueue<UserSessionAbs> sessions = new LinkedTransferQueue<>();
		ConcurrentMap sessionMapTimeSort = new SharedMap<>(new RadixTreeMap<>());
		/** Ustvari skupine za niti */
		ThreadGroup parseGroup = new ThreadGroup("Parse");
		ThreadGroup timeSortGroup = new ThreadGroup("TimeSort");
		ThreadGroup saveGroup = new ThreadGroup("SaveToDataBase");
		/** Ustavi niti ter jim podaj komunikacijske vrste */
		List<Thread> listParseThreads = new LinkedList<>();
		for (int i = 0; i < 1; i++) {
			listParseThreads.add(new ParserThread(parseGroup, parsedLines, logParser));
		}
		List<Thread> listTimeSortThreads = new LinkedList<>();
		for (int i = 0; i < 1; i++) {
			listTimeSortThreads.add(new TimeSortThread(timeSortGroup, parsedLines, sessions, sessionMapTimeSort, argumentParser.getSessionTime()));
		}
		List<Thread> listSaveDBThread = new LinkedList<>();
		for (int i = 0; i < 1; i++) {
			listSaveDBThread.add(new SaveDataBaseThread(sessions, db));
		}
		/** Zazeni niti */
		for (Thread t : listParseThreads) {
			t.start();
		}
		for (Thread t : listTimeSortThreads) {
			t.start();
		}
		for (Thread t : listSaveDBThread) {
			t.start();
		}
		/** Pocakaj da se niti koncajo z delom */
		for (Thread t : listParseThreads) {
			t.join();
		}
		for (Thread t : listTimeSortThreads) {
			t.join();
		}
		for (Thread t : listSaveDBThread) {
			t.join();
		}
	}

	@Override
	public void close() throws Exception {
		if (db != null) {
			db.close();
		}
		if (logParser != null) {
			logParser.close();
		}
	}
}
