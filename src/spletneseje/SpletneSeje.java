package spletneseje;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.kohsuke.args4j.CmdLineException;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import datastruct.RadixTree;
import fields.ncsa.RequestLine;
import parser.ParsedLine;
import parser.AbsParser;
import parser.ArgsParser;
import parser.FieldType;
import parser.IISParser;
import parser.NCSAParser;
import parser.W3CParser;

import analyzer.LogAnalyzer;

public class SpletneSeje {

	private ArgsParser argsParser;
	private AbsParser logParser;
	private SqlJetDb outDataBase;

	@SuppressWarnings("deprecation")
	public SpletneSeje(String[] args) throws CmdLineException, NullPointerException, ParseException, SqlJetException, IOException {
		//Parsanje vhodnih argumentov
		argsParser = new ArgsParser(args);

		//Preveri format in nastavi tipe polji v datoteki
		switch ((argsParser.getLogFormat() != null) ? argsParser.getLogFormat() : "") {
		case "":
			LogAnalyzer analyzer = new LogAnalyzer(argsParser.getInputFilePath());
			switch (analyzer.getLogFileType()) {
			case NCSA:
				logParser = new NCSAParser();
				// TODO S pomocjo analizatorja preveri tipe polji v zpisu, vrnjene vrednosti nastavi parserju
				logParser.openFile(analyzer.getOpendFile());
				break;
			case W3C:
				logParser = new W3CParser();
				logParser.openFile(analyzer.getOpendFile());
				break;
			case IIS:
				logParser = new IISParser();
				// TODO S pomocjo analizatorja preveri tipe polji v zpisu, vrnjene vrednosti nastavi parserju
				logParser.openFile(analyzer.getOpendFile());
				break;
			default:
				throw new ParseException("Unknow format of input log file!!!", 0);
			}
			break;
		case "COMMON":
			logParser = new NCSAParser();
			((NCSAParser) logParser).setFieldType(FieldType.createCommonLogFormat());
			logParser.openFile(argsParser.getInputFilePath());
			break;
		case "COMBINED":
			logParser = new NCSAParser();
			LogAnalyzer analyz = new LogAnalyzer(argsParser.getInputFilePath());
			((NCSAParser) logParser).setFieldType(FieldType.createCombinedLogFormat(analyz.hasCombinedCookie()));
			logParser.openFile(analyz.getOpendFile());
			break;
		case "EXTENDED":
			logParser = new W3CParser();
			logParser.openFile(argsParser.getInputFilePath());
			break;
		case "IIS":
			logParser = new IISParser();
			// TODO analiziraj zapise, ter vrni tipe polji s pomocjo analizatorja
			break;
		default:
			// TODO Preveri ali je uporabnik podal zapise za IIS ali NCSA
			logParser = new NCSAParser();
			((NCSAParser) logParser).setFieldType(FieldType.createCustomLogFormat(argsParser.getLogFormat()));
			logParser.openFile(argsParser.getInputFilePath());
		}

		//Nastavi format datuma
		if(argsParser.getDateFormat() != null) {
			if(logParser instanceof NCSAParser) {
				((NCSAParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else if(logParser instanceof W3CParser) {
				((W3CParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else {
				((IISParser) logParser).setDateFormat(argsParser.getDateFormat(), argsParser.getLocale());
			}
		}

		//Nastavi format ure
		if(argsParser.getTimeFormat() != null) {
			if(logParser instanceof NCSAParser) {
				System.err.println("ignoring -tf \""+argsParser.getTimeFormat()+"\"");
			} else if(logParser instanceof W3CParser) {
				((W3CParser) logParser).setTimeFormat(argsParser.getDateFormat(), argsParser.getLocale());
			} else {
				((IISParser) logParser).setTimeFormat(argsParser.getDateFormat(), argsParser.getLocale());
			}
		}

		//Ustvari novo izhodno datoteko
		File outFile = new File(argsParser.getOutputFilePath());
		if(argsParser.deleteOutputFile()) outFile.delete();
		outDataBase = SqlJetDb.open(outFile, true);

		// TODO izdelaj podatkovno bazo oziroma, ce datoteka ze obstaja preveri ali uporablja pravilne tabele, če temu ni tako javi napako
	}

	public void run() throws ParseException, NullPointerException, IOException {
		// FIXME metoda se ni pravilno implementirana
		//RadixTree<ParsedLine> data = new RadixTree<>();
		ArrayList<String> list = new ArrayList<>();
		for(ParsedLine tmp = logParser.parseLine(); tmp != null; tmp = logParser.parseLine()) {
			String sTmp = tmp.getExtension();
			int index = list.indexOf(sTmp);
			if(index == -1) {
				System.out.println(logParser.getPos()+" "+sTmp);
				list.add(sTmp);
			}
		}
		list.stream().forEach((s) -> System.out.println(s));
	}

	public static void main(String[] args) {
		try {
			new SpletneSeje(args).run();
		} catch (CmdLineException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.exit(2);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(3);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(4);
		} catch (SqlJetException e) {
			System.out.println(e.getMessage());
			System.exit(5);
		}
	}

}
