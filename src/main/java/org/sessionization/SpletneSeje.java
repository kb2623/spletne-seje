package org.sessionization;

import org.datastruct.ArraySet;
import org.kohsuke.args4j.CmdLineException;
import org.sessionization.analyzer.LogAnalyzer;
import org.sessionization.fields.FieldType;
import org.sessionization.parser.*;
import org.sessionization.parser.datastruct.RequestDump;
import org.sessionization.parser.datastruct.WebPageRequestDump;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

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

		/** Dodaj jar datoeke */
		Set<URL> set = new HashSet<>();
		if (argsParser.getDriverUrl() != null) set.add(argsParser.getDriverUrl());
		if (argsParser.getDialect() != null) set.add(argsParser.getDialect());
		UrlLoader loader = new UrlLoader(set.toArray(new URL[set.size()]));

		/** Ustvari dinamicne razrede */
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

		/** Nastavi nastavitve za hibernate */
		Properties props = new Properties();
		props.setProperty("hibernate.current_session_context_class", "thread");
		props.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
		props.setProperty("hibernate.connection.driver_class", argsParser.getDriverClass());
		props.setProperty("hibernate.dialect", argsParser.getDialectClass());
		props.setProperty("hibernate.connection.url", argsParser.getDatabaseUrl().toString());
		if (argsParser.getUserName() != null) props.setProperty("hibernate.connection.username", argsParser.getUserName());
		if (argsParser.getPassWord() != null) props.setProperty("hibernate.connection.password", argsParser.getPassWord());
		props.setProperty("hibernate.connection.pool_size", String.valueOf(argsParser.getConnectoinPoolSize()));
		props.setProperty("hibernate.show_sql", String.valueOf(argsParser.isShowSql()));
		props.setProperty("hibernate.format_sql", String.valueOf(argsParser.isShowSqlFormat()));
		props.setProperty("hibernate.hbm2ddl.auto", argsParser.getOperation().getValue());

		/** Ustvari povezavo do podatkovne baze, ter ustvari tabele */
		db = new HibernateUtil(props, loader, classes);
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
			System.out.println(e.getLocalizedMessage());
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
