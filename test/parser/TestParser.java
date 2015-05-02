package parser;

import java.io.File;
import java.io.StringReader;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import fields.Cookie;
import fields.ncsa.RequestLine;
import fields.w3c.MetaData;
import fields.UserAgent;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("deprecation")
public class TestParser {

	private String pathNCSACombined;
	private String pathNCSACommon;
	@SuppressWarnings("unused")
	private String pathW3C;
	@SuppressWarnings("unused")
	private String pathIIS;

	public TestParser() {
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) {
			pathNCSACombined = "C:\\Users\\mks19_000\\workspace\\spletne-seje\\Logs\\Combined\\access_log";
			pathNCSACommon = "C:\\Users\\mks19_000\\workspace\\spletne-seje\\Logs\\Common\\logCommon";
			pathW3C = "";
			pathIIS = "";
		} else {
			pathNCSACombined = "/home/klemen/workspace/spletne-seje/Logs/Combined/access_log";
			pathNCSACommon = "/home/klemen/workspace/spletne-seje/Logs/Common/logCommon";
			pathW3C = "";
			pathIIS = "";
		}
	}

	private void printNiz(String niz) {
		System.out.println("\n"+niz);
	}

	@Test
	public void testNCSAParserCommon() {
		printNiz("testNCSAParserCommon");
		NCSAParser parser = new NCSAParser();
		try {
			//Odpri datoteko
			parser.openFile(pathNCSACommon);
			//Nastavi tipe podatkov
			parser.setFieldType(FieldType.createCommonLogFormat());
			//Pridobi podatke
			ParsedLine list = parser.parseLine();
			list.getMap().values().stream().
				forEach((f) -> {
					if(f == null) {
						assert false;
					} else {
						System.out.print(f.izpis()+" || ");
					}
			});
			System.out.println();
			//Zapri datoteko
			parser.closeFile();
		} catch(NullPointerException | ParseException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testNCSAParserCombinedWithSetFormat() {
		printNiz("testNCSAParserCombinedWithSetFormat");
		NCSAParser parser = new NCSAParser();
		try {
			//Odpri datoteko
			parser.openFile(pathNCSACombined);
			//Nastavi tipe podatkov
			parser.setFieldType(FieldType.createCombinedLogFormat());
			//Nastavi format datuma
			parser.setDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
			//Pridobi podatke
			ParsedLine list = parser.parseLine();
			list.getMap().values().stream().
				forEach((f) -> {
					if(f == null) {
						assert false;
					} else {
						System.out.print(f.izpis()+" || ");
					}
			});
			System.out.println();
			//Zapri datoteko
			parser.closeFile();
		} catch(NullPointerException | ParseException | IOException e) {
		}
	}

	@Test
	public void testNCSAParserCombined() {
		printNiz("testNCSAParserCombined");
		NCSAParser parser = new NCSAParser();
		try {
			//Odpri datoteko
			parser.openFile(pathNCSACombined);
			//Nastavi tipe podatkov
			parser.setFieldType(FieldType.createCombinedLogFormat());
			//Pridobi podatke
			ParsedLine list = parser.parseLine();
			list.getMap().values().stream().
				forEach((f) -> {
					if(f == null) {
						assert false;
					} else {
						System.out.print(f.izpis()+" || ");
					}
			});
			System.out.println();
			//Zapri datoteko
			parser.closeFile();
		} catch(NullPointerException | ParseException | IOException e) {
		}
	}

	@Test
	public void testNCSAParserCombinedWithCookie() {
		printNiz("testNCSAParserCombinedWithCookie");
		NCSAParser parser = new NCSAParser();
		String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
		try {
			//Odpri datoteko
			parser.openFile(new StringReader(testNiz));
			//Nastavi tipe podatkov
			List<FieldType> listType = FieldType.createCombinedLogFormat();
			//Dodatni atribut
			listType.add(FieldType.Cookie);
			parser.setFieldType(listType);
			//Pridobi podatke
			ParsedLine list = parser.parseLine();
			list.getMap().values().stream().forEach((f) -> {
				if(f == null) {
					assert false;
				} else if(f instanceof RequestLine) {
					URL r = ((RequestLine) f).getUrl();
					System.out.print(((RequestLine) f).getMethod().izpis() + " | " + r.getPath() + " | " + r.getQuery() + " | " + r.getProtocol() + " | ");
				} else {
					System.out.print(f.izpis()+" || ");
				}
			});
			System.out.println();
			//Zapri datoteko
			parser.closeFile();
		} catch(NullPointerException | ParseException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testUserAgent() {
		printNiz("testUserAgent");
		String testString = "Mozilla/5.0+(compatible;+Yahoo!+Slurp/3.0;+http://help.yahoo.com/help/us/ysearch/slurp)";
		UserAgent userAgent = new UserAgent(testString, UserAgent.Type.W3C);
		String testString2 = "Mozilla/5.0+(Windows;+U;+Windows+NT+5.1;+en-US;+rv:1.9.0.16)+Gecko/2010010414+Firefox/3.0.16+Flock/2.5.6";
		UserAgent userAgent2 = new UserAgent(testString2, UserAgent.Type.W3C);
		String testString3 = "Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 520)";
		UserAgent userAgent3 = new UserAgent(testString3, UserAgent.Type.NCSA);
		String testString4 = "facebookexternalhit/1.1 (+http://www.facebook.com/externalhit_uatext.php)";
		UserAgent userAgent4 = new UserAgent(testString4, UserAgent.Type.NCSA);
		String testString5 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36";
		UserAgent userAgent5 = new UserAgent(testString5, UserAgent.Type.NCSA);
		System.out.printf("%s%n%s%n%s%n%s%n%s%n", userAgent.izpis(), userAgent2.izpis(), userAgent3.izpis(), userAgent4.izpis(), userAgent5.izpis());
	}

	@Test
	public void testCookie() {
		printNiz("testCookie");
		String testString = "USERID=CustomerA;IMPID=01234";
		Cookie cookie = new Cookie(testString, Cookie.Type.NCSA);
		String testString2 = "USERID=CustomerA;+IMPID=01234";
		Cookie cookie2 = new Cookie(testString2, Cookie.Type.W3C);
		String testString3 = "-";
		Cookie cookie3 = new Cookie(testString3, Cookie.Type.W3C);
		Cookie cookie4 = new Cookie(testString3, Cookie.Type.NCSA);
		String testString4 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		Cookie cookie5 = new Cookie(testString4, Cookie.Type.W3C);
		assertEquals("USERID = CustomerA\nIMPID = 01234\n", cookie.izpis());
		assertEquals("USERID = CustomerA\nIMPID = 01234\n", cookie2.izpis());
		assertEquals("-", cookie3.izpis());
		assertEquals("-", cookie4.izpis());
		assertEquals("referencna = -\n__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa\nGAMBIT_ID = 89.142.126.214-4231287712.29944903\nASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM\n__utmb = 237691092.48.10.1238543004\n__utmc = 237691092\n__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8\nproductsForComparison = 712150%21701041%21610119%21403083\n", cookie5.izpis());
	}

	@Test
	public void testW3C() {
		printNiz("testW3C");
		W3CParser parserW3C = new W3CParser();
		String testNiz = "#Software: Microsoft Internet Information Services 6.0\n"
				+"#Version: 1.0\n"
				+"#Date: 2006-10-22 22:17:15\n"
				+"#Fields: date time s-sitename s-computername s-ip cs-method cs-uri-stem cs-uri-query s-port cs-username c-ip cs-version cs(User-Agent) cs(Cookie) cs(Referer) cs-host sc-status sc-substatus sc-win32-status sc-bytes cs-bytes time-taken\n"
				+"2006-10-22 22:17:15 W3SVC979 LOCQUE 196.35.73.189 GET /robots.txt - 80 - 66.249.72.144 HTTP/1.1 Mozilla/5.0+(compatible;+Googlebot/2.1;++http://www.google.com/bot.html) - - www.ibcsa.com 404 0 2 1830 246 265\n";
		try{
			//Odpri datoteko
			parserW3C.openFile(new StringReader(testNiz));
			//pridobi podatke
			ParsedLine list = parserW3C.parseLine();
			assertEquals(1, list.getMap().size());
			assertEquals(list.getMap().get(FieldType.MetaData).getClass(), MetaData.class);
			list = parserW3C.parseLine();
			assertEquals(1, list.getMap().size());
			assertEquals(list.getMap().get(FieldType.MetaData).getClass(), MetaData.class);
			list = parserW3C.parseLine();
			assertEquals(1, list.getMap().size());
			assertEquals(list.getMap().get(FieldType.MetaData).getClass(), MetaData.class);
			list = parserW3C.parseLine();
			assertEquals(1, list.getMap().size());
			assertEquals(list.getMap().get(FieldType.MetaData).getClass(), MetaData.class);
			ParsedLine list1 = parserW3C.parseLine();
			assertEquals(22, list1.getMap().values().size());
			list1.getMap().values().stream().map((f) -> {
				assertNotNull(f);
				return f;
			}).forEach((f) -> System.out.print(f.izpis()+" || "));
			System.out.println();
			//Zapri datoteko
			parserW3C.closeFile();
		} catch(ParseException | IOException e) {
		}
	}

	@Test
	public void testW3CTwo() {
		printNiz("testW3CTwo");
		W3CParser parserW3C = new W3CParser();
		String testNiz = "#Software: Microsoft Internet Information Services 6.0\n"
				+"#Version: 1.0\n"
				+"#Date: 2009-04-01 00:00:00\n"
				+"#Fields: date time s-sitename s-computername s-ip cs-method cs-uri-stem cs-uri-query s-port cs-username c-ip cs-version cs(User-Agent) cs(Cookie) cs(Referer) cs-host sc-status sc-substatus sc-win32-status sc-bytes cs-bytes time-taken\n"
				+"2009-03-31 23:59:59 W3SVC1779542266 GTWEB1 217.72.80.140 GET /oddelki/racunalniskiDodatki/dept.asp dept_id=2181 80 - 89.142.123.239 HTTP/1.1 Mozilla/5.0+(Windows;+N;+Windows+NT+6.0;+sl;+rv:1.9.0.1)+Gecko/2008070208+Firefox/3.0.1 __utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092 http://www.enaa.com/oddelki/racunalnistvo/izd_3298_712150_HP_LaserJet_M1120_MFP www.enaa.com 200 0 0 69359 901 562\n";
		try{
			//Odpri datoteko
			parserW3C.openFile(new StringReader(testNiz));
			//pridobi podatke
			ParsedLine list;
			assertEquals(1, parserW3C.parseLine().getMap().size());
			assertEquals(1, parserW3C.parseLine().getMap().size());
			assertEquals(1, parserW3C.parseLine().getMap().size());
			assertEquals(1, parserW3C.parseLine().getMap().size());
			list = parserW3C.parseLine();
			assertEquals(22, list.getMap().values().size());
			list.getMap().values().stream().map((f) -> {
				assertNotNull(f);
				return f;
			}).forEach((f) -> System.out.print(f.izpis()+" || "));
			System.out.println();
			//Zapri datoteko
			parserW3C.closeFile();
		} catch(ParseException | IOException e) {
		}
	}

	@Test
	public void testCookieEquals() {
		printNiz("testCookieEquals");
		String testString = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		String testString2 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;GAMBIT_ID=89.142.126.214-4231287712.29944903;referencna=;__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;productsForComparison=712150%21701041%21610119%21403083;ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;__utmb=237691092.48.10.1238543004;__utmc=237691092";
		String testString3 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;GAMBIT_ID=89.142.126.214-4231287712.29944903;referencna=;productsForComparison=712150%21701041%21610119%21403083;ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;__utmb=237691092.48.10.1238543004;__utmc=237691092";
		Cookie cookie = new Cookie(testString, Cookie.Type.W3C);
		Cookie cookie2 = new Cookie(testString2, Cookie.Type.NCSA);
		Cookie cookie3 = new Cookie(testString3, Cookie.Type.NCSA);
		assertTrue(cookie.equals(cookie2));
		assertFalse(cookie.equals(cookie3));
		assertFalse(cookie.equals(new UserAgent("-", UserAgent.Type.NCSA)));
		assertFalse(cookie.equals(new UserAgent("-", UserAgent.Type.W3C)));
	}

	@Test
	public void testSQLight() throws SqlJetException {
		printNiz("testSQLight");
		//Odpri datoteko
		File dbFile = new File("/home/klemen/workspace/spletne-seje/dbFile");
		//Izbriši datoteko, če že obstaja
		dbFile.delete();
		//Ustvari novo SQLight bazo
		SqlJetDb jetDb = SqlJetDb.open(dbFile, true);
		//začni s transakcijami
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			//Kreiranje tabele
			jetDb.createTable("CREATE TABLE employees (second_name TEXT NOT NULL PRIMARY KEY , first_name TEXT NOT NULL, date_of_birth INTEGER NOT NULL)");
			//Če transakcija uspe
			jetDb.commit();
		} catch(Exception e) {
			//Če transakcija ne uspe
			jetDb.rollback();
		}
		//začni z novimi transakcijami
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			//Ustvarjanje nove table v PB
			ISqlJetTable table = jetDb.getTable("employees");
			//Dodajanje zapisov v PB
			table.insert("Prochaskova", "Elena", Calendar.getInstance().getTimeInMillis());
			table.insert("Scherbina", "Sergei", Calendar.getInstance().getTimeInMillis());
			table.insert("Vadishev", "Semen", Calendar.getInstance().getTimeInMillis());
			table.insert("Sinjushkin", "Alexander", Calendar.getInstance().getTimeInMillis());
		    table.insert("Stadnik", "Dmitry", Calendar.getInstance().getTimeInMillis());
		    table.insert("Kitaev", "Alexander", Calendar.getInstance().getTimeInMillis());
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		//začni z novimi transakcijami
		jetDb.beginTransaction(SqlJetTransactionMode.WRITE);
		try {
			//Ustvarjanje nove table v PB
			ISqlJetTable table = jetDb.getTable("employees");
			ISqlJetCursor cursor = table.open();
			while(!cursor.eof()) {
				System.out.println(cursor.getRowId()+" : "+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getInteger(2));
				cursor.next();
			}
			jetDb.commit();
		} catch(Exception e) {
			jetDb.rollback();
		}
		//Ko končaš z delom
		jetDb.close();
		dbFile.delete();
	}

	@Test
	public void testLoL() {
		printNiz("testLoL");
		System.out.println(System.getProperty("os.name"));
	}

	@Test
	public void testDateParsing() {
		printNiz("TestDateParsing");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);
		LocalDateTime dateTime = LocalDateTime.parse("26/Jul/2002:12:11:52 +0000", formatter);
		assertEquals(26, dateTime.getDayOfMonth());
	}

}
