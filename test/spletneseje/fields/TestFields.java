package spletneseje.fields;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import spletneseje.fields.w3c.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("deprecation")
public class TestFields {

	@Test
	public void testUserAgent() {
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
		String testString = "USERID=CustomerA;IMPID=01234";
		Cookie cookie = new Cookie(testString, Cookie.Type.NCSA);
		String testString2 = "USERID=CustomerA;+IMPID=01234";
		Cookie cookie2 = new Cookie(testString2, Cookie.Type.W3C);
		String testString3 = "-";
		Cookie cookie3 = new Cookie(testString3, Cookie.Type.W3C);
		Cookie cookie4 = new Cookie(testString3, Cookie.Type.NCSA);
		String testString4 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		Cookie cookie5 = new Cookie(testString4, Cookie.Type.W3C);
		assertEquals("[[USERID = CustomerA][IMPID = 01234]]", cookie.izpis());
		assertEquals("[[USERID = CustomerA][IMPID = 01234]]", cookie2.izpis());
		assertEquals("[-]", cookie3.izpis());
		assertEquals("[-]", cookie4.izpis());
		assertEquals("[[referencna = -][__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa][GAMBIT_ID = 89.142.126.214-4231287712.29944903][ASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM][__utmb = 237691092.48.10.1238543004][__utmc = 237691092][__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8][productsForComparison = 712150%21701041%21610119%21403083]]", cookie5.izpis());
	}

	@Test
	public void testCookieEquals() {
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
	public void testDateParsing() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);
		LocalDateTime dateTime = LocalDateTime.parse("26/Jul/2002:12:11:52 +0000", formatter);
		assertEquals(26, dateTime.getDayOfMonth());
	}
}
