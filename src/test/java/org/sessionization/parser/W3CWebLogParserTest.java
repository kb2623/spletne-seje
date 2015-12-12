package org.sessionization.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.fields.w3c.Date;
import org.sessionization.fields.w3c.MetaData;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings("deprecation")
public class W3CWebLogParserTest {

	private W3CWebLogParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new W3CWebLogParser();
	}

	@Test
	public void testParseStringOne() {
		String testNiz = "#Software: Microsoft Internet Information Services 6.0\n"
				+ "#Version: 1.0\n"
				+ "#Date: 2006-10-22 22:17:15\n"
				+ "#Fields: date time s-sitename s-computername s-ip cs-method cs-uri-stem cs-uri-query s-port cs-username c-ip cs-version cs(User-Agent) cs(Cookie) cs(Referer) cs-host sc-status sc-substatus sc-win32-status sc-bytes cs-bytes time-taken\n"
				+ "2006-10-22 22:17:15 W3SVC979 LOCQUE 196.35.73.189 GET /robots.txt - 80 - 66.249.72.144 HTTP/1.1 Mozilla/5.0+(compatible;+Googlebot/2.1;++http://www.google.com/bot.html) - - www.ibcsa.com 404 0 2 1830 246 265\n";
		try {
			//Odpri datoteko
			parser.openFile(new StringReader(testNiz));
			//pridobi podatke
			ParsedLine list = parser.parseLine();
			assertEquals(list.get(0).getClass(), MetaData.class);
			list.forEach(Assert::assertNotNull);
			assertEquals("[#Software: | Microsoft | Internet | Information | Services | 6.0]", list.izpis());
			list = parser.parseLine();
			assertEquals(list.get(0).getClass(), MetaData.class);
			list.forEach(Assert::assertNotNull);
			assertEquals("[#Version: | 1.0]", list.izpis());
			list = parser.parseLine();
			assertEquals(list.get(0).getClass(), MetaData.class);
			list.forEach(Assert::assertNotNull);
			assertEquals("[#Date: | 2006-10-22 | 22:17:15]", list.izpis());
			list = parser.parseLine();
			assertEquals(list.get(0).getClass(), MetaData.class);
			list.forEach(Assert::assertNotNull);
			assertEquals("[#Fields: | date | time | s-sitename | s-computername | s-ip | cs-method | cs-uri-stem | cs-uri-query | s-port | cs-username | c-ip | cs-version | cs(User-Agent) | cs(Cookie) | cs(Referer) | cs-host | sc-status | sc-substatus | sc-win32-status | sc-bytes | cs-bytes | time-taken]", list.izpis());
			list = parser.parseLine();
			assertEquals(list.get(0).getClass(), Date.class);
			list.forEach(Assert::assertNotNull);
			assertEquals("[2006-10-22 | 22:17:15 | W3SVC979 | LOCQUE | Server 196.35.73.189 | GET | /robots.txt | [-] | Server port 80 | - | Client 66.249.72.144 | HTTP version 1.1 | Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html) | [-] | - | www.ibcsa.com | 404 | 0 | 2 | 1830 | 246 | 265000]", list.izpis());
			//Zapri datoteko
			parser.closeFile();
		} catch (ParseException | IOException e) {
			fail();
		}
	}

	@Test
	public void testParseLineStringTwo() {
		String testNiz = "#Software: Microsoft Internet Information Services 6.0\n"
				+ "#Version: 1.0\n"
				+ "#Date: 2009-04-01 00:00:00\n"
				+ "#Fields: date time s-sitename s-computername s-ip cs-method cs-uri-stem cs-uri-query s-port cs-username c-ip cs-version cs(User-Agent) cs(Cookie) cs(Referer) cs-host sc-status sc-substatus sc-win32-status sc-bytes cs-bytes time-taken\n"
				+ "2009-03-31 23:59:59 W3SVC1779542266 GTWEB1 217.72.80.140 GET /oddelki/racunalniskiDodatki/dept.asp dept_id=2181 80 - 89.142.123.239 HTTP/1.1 Mozilla/5.0+(Windows;+N;+Windows+NT+6.0;+sl;+rv:1.9.0.1)+Gecko/2008070208+Firefox/3.0.1 __utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092 http://www.enaa.com/oddelki/racunalnistvo/izd_3298_712150_HP_LaserJet_M1120_MFP www.enaa.com 200 0 0 69359 901 562\n";
		try {
			//Odpri datoteko
			parser.openFile(new StringReader(testNiz));
			//pridobi podatke
			assertEquals("[#Software: | Microsoft | Internet | Information | Services | 6.0]", parser.parseLine().izpis());
			assertEquals("[#Version: | 1.0]", parser.parseLine().izpis());
			assertEquals("[#Date: | 2009-04-01 | 00:00:00]", parser.parseLine().izpis());
			assertEquals("[#Fields: | date | time | s-sitename | s-computername | s-ip | cs-method | cs-uri-stem | cs-uri-query | s-port | cs-username | c-ip | cs-version | cs(User-Agent) | cs(Cookie) | cs(Referer) | cs-host | sc-status | sc-substatus | sc-win32-status | sc-bytes | cs-bytes | time-taken]", parser.parseLine().izpis());
			assertEquals("[2009-03-31 | 23:59:59 | W3SVC1779542266 | GTWEB1 | Server 217.72.80.140 | GET | /oddelki/racunalniskiDodatki/dept.asp | [[dept_id = 2181]] | Server port 80 | - | Client 89.142.123.239 | HTTP version 1.1 | Mozilla/5.0 (Windows; N; Windows NT 6.0; sl; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1 | [[__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8][GAMBIT_ID = 89.142.126.214-4231287712.29944903][referencna = -][__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa][productsForComparison = 712150%21701041%21610119%21403083][ASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM][__utmb = 237691092.48.10.1238543004][__utmc = 237691092]] | www.enaa.com/oddelki/racunalnistvo/izd_3298_712150_HP_LaserJet_M1120_MFP | www.enaa.com | 200 | 0 | 0 | 69359 | 901 | 562000]", parser.parseLine().izpis());
			//Zapri datoteko
			parser.closeFile();
		} catch (ParseException | IOException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}

	@Test
	public void testParseLineStringTryResource() {
		String testNiz = "#Software: Microsoft Internet Information Services 6.0\n"
				+ "#Version: 1.0\n"
				+ "#Date: 2009-04-01 00:00:00\n"
				+ "#Fields: date time s-sitename s-computername s-ip cs-method cs-uri-stem cs-uri-query s-port cs-username c-ip cs-version cs(User-Agent) cs(Cookie) cs(Referer) cs-host sc-status sc-substatus sc-win32-status sc-bytes cs-bytes time-taken\n"
				+ "2009-03-31 23:59:59 W3SVC1779542266 GTWEB1 217.72.80.140 GET /oddelki/racunalniskiDodatki/dept.asp dept_id=2181 80 - 89.142.123.239 HTTP/1.1 Mozilla/5.0+(Windows;+N;+Windows+NT+6.0;+sl;+rv:1.9.0.1)+Gecko/2008070208+Firefox/3.0.1 __utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092 http://www.enaa.com/oddelki/racunalnistvo/izd_3298_712150_HP_LaserJet_M1120_MFP www.enaa.com 200 0 0 69359 901 562\n";
		try (W3CWebLogParser parser1 = new W3CWebLogParser()) {
			parser1.openFile(new StringReader(testNiz));
			assertEquals("[#Software: | Microsoft | Internet | Information | Services | 6.0]", parser1.parseLine().izpis());
			assertEquals("[#Version: | 1.0]", parser1.parseLine().izpis());
			assertEquals("[#Date: | 2009-04-01 | 00:00:00]", parser1.parseLine().izpis());
			assertEquals("[#Fields: | date | time | s-sitename | s-computername | s-ip | cs-method | cs-uri-stem | cs-uri-query | s-port | cs-username | c-ip | cs-version | cs(User-Agent) | cs(Cookie) | cs(Referer) | cs-host | sc-status | sc-substatus | sc-win32-status | sc-bytes | cs-bytes | time-taken]", parser1.parseLine().izpis());
			assertEquals("[2009-03-31 | 23:59:59 | W3SVC1779542266 | GTWEB1 | Server 217.72.80.140 | GET | /oddelki/racunalniskiDodatki/dept.asp | [[dept_id = 2181]] | Server port 80 | - | Client 89.142.123.239 | HTTP version 1.1 | Mozilla/5.0 (Windows; N; Windows NT 6.0; sl; rv:1.9.0.1) Gecko/2008070208 Firefox/3.0.1 | [[__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8][GAMBIT_ID = 89.142.126.214-4231287712.29944903][referencna = -][__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa][productsForComparison = 712150%21701041%21610119%21403083][ASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM][__utmb = 237691092.48.10.1238543004][__utmc = 237691092]] | www.enaa.com/oddelki/racunalnistvo/izd_3298_712150_HP_LaserJet_M1120_MFP | www.enaa.com | 200 | 0 | 0 | 69359 | 901 | 562000]", parser1.parseLine().izpis());
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testParseLineFileTryResource() {
		try (AbsWebLogParser parser1 = new W3CWebLogParser(Locale.US, new File[]{new File(ClassLoader.getSystemResource("ex100614.log").getFile())})) {
			parser1.forEach(System.out::println);
		} catch (IOException e) {
			fail();
		}
	}

	@Test
	public void testParseLineFileOne() {
		try {
			parser.openFile(new File[]{new File(ClassLoader.getSystemResource("ex080814.log").getFile())});
			parser.forEach(System.out::println);
			parser.closeFile();
		} catch (IOException e) {
			fail();
		}
	}
}