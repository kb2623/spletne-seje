package org.sessionization;

import org.hibernate.cache.spi.QueryKey;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.sessionization.analyzer.LogAnalyzer;
import org.sessionization.fields.*;
import org.sessionization.fields.cookie.CookieKey;
import org.sessionization.fields.cookie.CookiePair;
import org.sessionization.fields.ncsa.RequestLine;
import org.sessionization.fields.query.UriQueryKey;
import org.sessionization.fields.query.UriQueryPair;
import org.sessionization.parser.*;
import org.sessionization.parser.datastruct.RequestDump;
import org.sessionization.parser.datastruct.WebPageRequestDump;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

public class SpletneSeje {

	private ArgsParser argsParser;
	private AbsParser logParser;
	private HibernateUtil db;
	/**
	 *
	 * @param args
	 * @throws CmdLineException
	 * @throws NullPointerException
	 * @throws ParseException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public SpletneSeje(String[] args) throws CmdLineException, NullPointerException, ParseException, IOException, ClassNotFoundException, URISyntaxException {
		/** Parsanje vhodnih argumentov */
		argsParser = new ArgsParser(args);

		// todo nastavi nastavitve za hibernate

		/** Preveri format in nastavi tipe polji v datoteki */
		switch ((argsParser.getLogFormat() != null) ? argsParser.getLogFormat().get(0) : "") {
		case "":
			LogAnalyzer analyzer = new LogAnalyzer(argsParser.getInputFilePath());
			switch (analyzer.getLogFileType()) {
			case NCSA:
				logParser = new NCSAParser();
				break;
			case W3C:
				logParser = new W3CParser();
				break;
			case IIS:
				logParser = new IISParser();
				break;
			default:
				throw new ParseException("Unknow format of input log file!!!", 0);
			}
			break;
		case "COMMON":
			logParser = new NCSAParser(LogFormats.CommonLogFormat.create(LogAnalyzer.hasCombinedCookie()));
			break;
		case "COMBINED":
			logParser = new NCSAParser(LogFormats.CombinedLogFormat.create(LogAnalyzer.hasCombinedCookie()));
			break;
		case "EXTENDED":
			logParser = new W3CParser();
			break;
		case "IIS":
			logParser = new IISParser();
			break;
		default:
			logParser = new NCSAParser();
			logParser.setFieldType(LogFormats.CustomLogFormat.create(argsParser.getLogFormat()));
		}
		logParser.openFile(argsParser.getInputFilePath());

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

		// todo ce imamo Extended log format potem moramo drugace pridobiti tipe polji

		/** Ustvari dinamicne razrede */
		UrlLoader loader;
		if (argsParser.getDriverUrl() != null) {
			URL[] url;
			if (argsParser.getDialect() == null) {
				url = new URL[] {
						argsParser.getDriverUrl()
				};
			} else {
				url = new URL[] {
						argsParser.getDriverUrl(),
						argsParser.getDialect()
				};
			}
			url[0] = argsParser.getDriverUrl();
			loader = new UrlLoader(url);
		} else {
			loader = new UrlLoader(new URL[]{});
		}
		loader.defineClass(WebPageRequestDump.getClassName(), WebPageRequestDump.dump(logParser.getFieldType()));
		loader.defineClass(RequestDump.getClassName(), RequestDump.dump(logParser.getFieldType()));

		/** Izdelaj tabeli razredov za podatkovno bazo */
		Set<Class> classes = new HashSet<>();
		for (FieldType f : logParser.getFieldType()) {
			for (Class c : f.getDependencies()) classes.add(c);
			classes.add(f.getClassType());
		}
		classes.add(loader.loadClass(RequestDump.getClassName()));
		classes.add(loader.loadClass(WebPageRequestDump.getClassName()));

		/** Ustvari povezavo do podatkovne baze */
		db = new HibernateUtil(argsParser.getConfigFile(), loader, classes);
	}
	/**
	 *
	 * @throws ParseException
	 * @throws NullPointerException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void run() throws ParseException, NullPointerException, IOException, URISyntaxException {
		// todo glavna logika programa
	}
	/**
	 *
	 * @param args
	 */
	public static void main(String... args) {
		try {
			new SpletneSeje(args).run();
		} catch (CmdLineException e) {
			e.getParser().printUsage(System.out);
			e.printStackTrace();
			if (e.getCause() != null && e.getCause().getMessage() != null)
				System.err.println(e.getCause().getMessage());
			System.exit(1);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			System.exit(2);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(3);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(4);
		} catch (URISyntaxException e) {
			System.err.println(e.getMessage());
			System.exit(5);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(6);
		}
	}

}
